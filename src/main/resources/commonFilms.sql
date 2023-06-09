SELECT * FROM (
SELECT f.* FROM film f
LEFT JOIN like_ l ON l.film_id = f.ID
GROUP BY f.ID
ORDER BY COUNT (l.film_id)
) f, like_ l1, like_ l2
WHERE f.id = l1.film_id AND f.id = l2.film_id AND l1.user_id = 1 AND l2.user_id = 2

select f.* from film f
inner join like_ l on
f.id=l.film_id
where f.id in
(select film_id from like_
where user_id = 1 and film_id in
(select film_id from like_ where user_id = 2))
group by f.id
order by count(f.id) 