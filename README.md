# java-filmorate
### 1. ER диаграмма
![](ER%20Diagram%20filmorate.jpeg)

### 2. Примеры запросов
2.1 Получить список популярных фильмов, top 10
```sql
SELECT f.id FROM film f
LEFT JOIN likes l ON l.film_id = f.id
GROUP BY f.id
ORDER BY COUNT(1) DESC
LIMIT 10;
```

2.2 Получить список подтверждённых друзей
```sql
SELECT (CASE WHEN fr1.user_id_initiator = u1.id 
        THEN fr1.user_id_friend
        ELSE fr1.user_id_initiator END) friend_user_id 
FROM user u1
INNER JOIN friend_request fr1 ON (fr1.user_id_initiator = u1.id 
                                or fr1.user_id_friend = u1.id)
                                AND fr1.is_approved = true
WHERE u1.id = :user_id_1;
```

2.3 Получить список общих подтверждённых друзей
```sql
SELECT (CASE WHEN fr1.user_id_initiator = u1.id 
        THEN fr1.user_id_friend
        ELSE fr1.user_id_initiator END) friend_user_id FROM user u1
INNER JOIN friend_request fr1 ON (fr1.user_id_initiator = u1.id 
                                or fr1.user_id_friend = u1.id)
                                AND fr1.is_approved = true
WHERE u1.id = :user_id_1
INTERSECT
SELECT (CASE WHEN fr2.user_id_initiator = u2.id 
        THEN fr2.user_id_friend
        ELSE fr2.user_id_initiator END) friend_user_id FROM user u2
INNER JOIN friend_request fr2 ON (fr2.user_id_initiator = u2.id 
                                or fr2.user_id_friend = u2.id)
                                AND fr2.is_approved = true
WHERE u2.id = :user_id_2;
```

2.4 Получить список жанров для фильма
```sql
SELECT g.id, g.name FROM film f
INNER JOIN film_genre fg ON fg.film_id = f.film_id
INNER JOIN genre g ON g.id = fg.genre_id;
```
