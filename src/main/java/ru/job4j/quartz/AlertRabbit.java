package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static void main(String[] args) {
        try (Connection connection = init()) {
            List<Long> store = new ArrayList<>();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            connection.close();
            System.out.println(store);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static Connection init() throws IOException, ClassNotFoundException, SQLException {
        Class.forName(propertiesRabbit().getProperty("driver"));
        String url = propertiesRabbit().getProperty("url");
        String login = propertiesRabbit().getProperty("username");
        String password = propertiesRabbit().getProperty("password");
        return DriverManager.getConnection(
                url, login, password);
    }

    public static Properties propertiesRabbit() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream("rabbit.properties")) {
            properties.load(in);
        }
        return properties;
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
            try (PreparedStatement statement =
                         init().prepareStatement("INSERT INTO rabbit (created_date) values (?)")) {
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement.execute();
            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}