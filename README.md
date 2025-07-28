# Grocery Store Management Web Application
A web app to manage employees, products, and sales data for a grocery store

## About
The Grocery Store Management Web Application is designed for **store employees and managers** to efficiently handle:
  - Product inventory
  - Customer orders
  - Employee logins

This application streamlines in-store operations by providing a centralized portal for managing key store date through a brower accessible interface.


## Features

### User Authentication
- Employee login system
- Session based access control
### Product Management
  - Add/Edit/Delete Products
  - Upload product images
  - Sort/search through inventory
### Order Management
  - View customer orders
  - Status update
### Database Integration
- All data stored in **MySQL** database


## Built With
* **Frontend:** HTML, CSS, JSP
* **Backend:** Java (Servlets), Tomcat 11
* **Database:** MySQL
* **Tools:** Eclipse IED, Git, GitHub, MySQL Workbench


## Getting Started
1. Download or Clone the repo for our Grocery Store Management Web Application (https://github.com/jiminpak03/CS3773Repo.git)
2. Import into Eclipse
3. Set up **Apache Tomcat 11 Server** and add the project to Tomcat
4. Start **MySQL Server** and execute the ProjectDB.sql file to create the database and insert test data
   - Make sure to upload the database into the correct MySQL connection:
     * Connection Hostname: 'localhost' and Port: '3306'
     * Set Username: 'root' and Password: 'admin'
5. Deploy and run the web application on Tomcat
6. Visit: http://localhost:8080/CS3773Repo
   * Login:
     - Username: admin123
     - Password: admin


## Usage
### Once running:
- Use the sidebar to navigate between **Home**, **Product Catalog**, **Orders**, and **Account** views.
- In **Product Catalog**, you can view available items, search by name, and add/edit/delete.
  * you can edit the product name, quantity, description, price, and sale percentage.
- In **Orders**, you can view customer orders, Execute Order to complete it, Update the Status of the order, and view more details.
- The **Account** page displays the currently logged in user's profile info, and logout of the account.
    
  


## Contributors
* Ivette Gonzalez
* Jennifer Salttery
* Ian Pak
* Katie Liu
* Josue Souza

  
