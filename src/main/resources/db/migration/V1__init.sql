-- Skema awal product-service, di-generate Hibernate dari entity (Okt 2026), lalu dirapikan.
-- Database yang sudah ada sebelum Flyway dipasang di-baseline di versi 1 (spring.flyway.baseline-on-migrate),
-- jadi file ini hanya dijalankan pada database kosong (CI, volume Docker baru).
-- JANGAN diubah: perubahan skema berikutnya = file baru V2__..., V3__..., dst.

create table branch_stocks (
    harvest_date date not null,
    stock integer not null,
    updated_at timestamp(6),
    branch_id uuid not null,
    id uuid not null,
    product_id uuid not null,
    primary key (id),
    unique (product_id, branch_id)
);

create table branches (
    is_active boolean not null,
    created_at timestamp(6) not null,
    id uuid not null,
    phone varchar(20),
    name varchar(100) not null unique,
    address TEXT not null,
    primary key (id)
);

create table categories (
    id uuid not null,
    name varchar(100) not null unique,
    icon_url varchar(500),
    primary key (id)
);

create table products (
    cost_price numeric(10,2) not null,
    freshness_days integer not null,
    is_active boolean not null,
    price numeric(10,2) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6),
    category_id uuid not null,
    id uuid not null,
    unit varchar(20) not null,
    name varchar(200) not null,
    image_url varchar(500),
    description TEXT,
    primary key (id)
);

alter table if exists branch_stocks
    add constraint FKjd2efw14mdnrrlyt8ro545bqb
    foreign key (branch_id)
    references branches;

alter table if exists branch_stocks
    add constraint FKijhehw5d4i5b3chj6q4wlij06
    foreign key (product_id)
    references products;

alter table if exists products
    add constraint FKog2rp4qthbtt2lfyhfo32lsw9
    foreign key (category_id)
    references categories;
