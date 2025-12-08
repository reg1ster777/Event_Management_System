create table if not exists checkin (
    checkin_id  int primary key auto_increment,
    activity_id int          not null,
    student_no  varchar(100) not null,
    name        varchar(100),
    phone       varchar(50),
    checkin_time datetime    not null default current_timestamp,
    method      varchar(50),
    constraint fk_checkin_activity foreign key (activity_id) references activity(activity_id),
    constraint uq_checkin unique (activity_id, student_no)
);
