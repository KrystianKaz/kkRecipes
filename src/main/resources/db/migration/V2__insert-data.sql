INSERT INTO daily_diet(id, date, added_by_user, calories, fat, carbohydrates, protein, description, diet_type,
 first_meal_id, second_meal_id, third_meal_id, completed)
values  (1, '2023-03-20', 1, 2000.03, 139.7, 88.63, 99.54, 'My first diet', 'DAILY', 1697823, 662880, 650660, 0),
        (2, '2023-03-21', 1, 2994.23, 205.09, 149.91, 139.04, 'Tuesday','DAILY', 636026, 649141, 636286, 0),
        (3, '2023-03-21', 2, 2502.69, 129.88, 230.34, 92.1, 'favourite','DAILY', 640275, 1697625, 658290, 0),
        (4, '2023-03-21', 2, 1999.94, 132.41, 143.48, 63.38, 'but high fat','DAILY', 664533, 1516713, 657011, 0),
        (5, '2023-03-21', 2, 2000.04, 95.75, 205.75, 79.02, 'balanced','DAILY', 657359, 668492, 642593, 0),
        (6, '2023-03-22', 2, 1999.89, 132.61, 126.4, 77.34, '60f/25c/15p','DAILY', 649300, 1697583, 715498, 0),
        (7, '2023-03-22', 2, 1999.97, 90.93, 218, 85.68, 'wednesday','DAILY', 648632, 641565, 715385, 0),
        (8, '2023-03-22', 1, 1999.93, 111.82, 177.89, 75.59, 'wednesday','DAILY', 1095797, 642681, 1697625, 0),
        (9, '2023-03-22', 2, 1999.88, 108.06, 157.46, 114.77, 'xyz','DAILY', 650431, 157399, 650255, 1),
        (10, '2023-03-23', 2, 2000.02, 93.54, 162.94, 132.22, 'high proteins','DAILY', 649431, 1697535, 644953, 1),
        (11, '2023-03-23', 1, 1999.93, 140.32, 70.63, 109.1, 'high fat','DAILY', 640275, 622598, 661126, 0),
        (12, '2023-03-24', 1, 2659.25, 181.01, 160.22, 102.83, 'cheat','DAILY', 664054, 1095806, 648983, 0),
        (13, '2023-03-24', 2, 2000.27, 117.35, 171.27, 67.18, 'friday','DAILY', 644848, 1697749, 657011, 1);

INSERT INTO user(id, account_creation_date, account_creation_time, active, email, password, user_roles, username)
values  (1, '2023-03-20', '12:00:00', true, 'admin@example.com', '$2a$10$OT07oJXozePV/DjtjBUPxOkCpWcYs9UcCnQUbEzb.6k8TC568qp7i', 'ADMIN', 'admin'),
        (2, '2023-03-21', '22:01:13', true, 'user@example.com', '$2a$10$.xVn1nlHBVb0YKSkEmdMYOFQAAW1you99ElCrf4kZx35ghp9CT6CW', 'USER', 'user');

INSERT INTO meal(id, meal_id, meal_title, still_liked, liked_by_user)
values  (1, 795751, 'Chicken Fajita Stuffed Bell Pepper', 1, 2),
        (2, 716426, 'Cauliflower, Brown Rice, and Vegetable Fried Rice', 1, 1),
        (3, 657359, 'Pumpkin Pie Smoothie', 1, 1),
        (4, 656544, 'Polenta gnocchi with savoy cabbage and cheese', 1, 1),
        (5, 631814, '$50,000 Burger', 1, 2),
        (6, 716627, 'Easy Homemade Rice and Beans', 1, 2),
        (7, 715497, 'Berry Banana Breakfast Smoothie', 0, 1),
        (8, 644387, 'Garlicky Kale', 0, 1),
        (9, 632426, 'Anticuchos Of White Seabass With Aji Chile Honey Marinade & Semilla Salsa', 1, 2);