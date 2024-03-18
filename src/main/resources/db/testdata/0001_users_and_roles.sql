insert into
    users(email, password, username, profile_picture, is_banned)
values
    ('admin@example.com', '{noop}adminpass', 'igoradmin', '21savage0.jpg', false),
    ('user@example.com', '{noop}userpass', 'useraccount', '21savage1.jpg', false),
    ('editor@example.com', '{noop}editorpass', 'editoraccount', '21savage2.jpg', false);

insert into
    user_role(name, description)
values
    ('ADMIN', 'pełne uprawnienia'),
    ('USER', 'podstawowe uprawnienia, możliwość oddawania głosów'),
    ('EDITOR', 'podstawowe uprawnienia + możliwość zarządzania treściami');

insert into
    user_roles(user_id, role_id)
values
    (1, 1),
    (2, 2),
    (3, 3);