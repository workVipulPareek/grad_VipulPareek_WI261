CREATE TABLE site (
    site_id SERIAL PRIMARY KEY,
    length INT,
    width INT,
    property_type VARCHAR(30),
    maintenance_paid BOOLEAN DEFAULT FALSE,
    maintenance_amount NUMERIC,
    owner_id INT REFERENCES owner(owner_id)
);

CREATE TABLE owner (
    owner_id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(15)
);

CREATE TABLE site_update_request (
    request_id SERIAL PRIMARY KEY,
    site_id INT,
    owner_id INT,
    new_length INT,
    new_width INT,
    status VARCHAR(20) DEFAULT 'PENDING'
);

Select * FROM owner;
SELECT * FROM site;
SELECT * FROM site_update_request;