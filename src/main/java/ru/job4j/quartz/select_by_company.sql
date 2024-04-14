select * from person where company_id != 5;


select p.name, comp.name from person as p
join company as comp on p.company_id = comp.id
group by comp.name, p.name


select COUNT(p.id), comp.name from person as p
join company as comp on p.company_id = comp.id
group by comp.name
order by MAX(p.id) desc
limit 2