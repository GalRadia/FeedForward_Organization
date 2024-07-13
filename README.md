
# FeedForward Association App

## Introduction

The FeedForward Association App is an Android application designed to facilitate  donation of leftover food from restaurants to associations in need. The app allows restaurants to manage their food donations, view past and active orders, and update donation details.

## Features
- Viewing and tracking past and activte orders bound for the association.
- Creating and managing new orders from the nearby avaliable restuersts
- Integration with google places API in order to find the best suitlbe resurents in your area
-  Presistent data retention both on client side and server side.
- Multi-language support and localization.
- Updating and change the orders on the go.

## Tech Stack
-   **Java based application using the MVVM desgin patten**
-   **Server communication based on the Retrofit framework**
-   **Integration with Google maps  and Places API**
-   **Usage of navigation UI and bottom navigation view**

## Future improvements
-   **Add notifications for adding or changing orders**
-  **Support in pagination** 
## Setup

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/GalRadia/FeedForward_Organization
    ```
    
2.  **Install Dependencies**:  
    Ensure that Gradle is installed and your environment is set up for Android development. Run:
```bash
 ./gradlew build
 ```

3.  **Configure Secrets **:
-   `Secrets.properties`: Ensure this file contains your Google API key
    
-   `Local.defaults.properties`: Set up default configurations here.
```
MAPS_API_KEY=YOUR_API_KEY
```
## Application Flow

[](https://github.com/TzachiPinhas/Miniapp_Restaurant#application-flow)

1.  **Registration/Login**: Association begin by registering for an account using an email.
2.  **Home Page**: Upon successful login, the home page displays the restaurants in radius of 5,15,30 KM from the association
3.  **Managing Donations**: After picking a restaurant you can create an order from the restaurant's storage
4.  **My Orders**: Association can track all the orders (Pending, Delivered  and Active)
5.  **Reviews **: After finishing an order the association can add a review to the restaurant.

# FeedForward Association App

## Introduction

The FeedForward Association App is an Android application designed to facilitate  donation of leftover food from restaurants to associations in need. The app allows restaurants to manage their food donations, view past and active orders, and update donation details.

## Features
- Viewing and tracking past and activte orders bound for the association.
- Creating and managing new orders from the nearby avaliable restuersts
- Integration with google places API in order to find the best suitlbe resurents in your area
-  Presistent data retention both on client side and server side.
- Multi-language support and localization.
- Updating and change the orders on the go.

## Tech Stack
-   **Java based application using the MVVM desgin patten**
-   **Server communication based on the Retrofit framework**
-   **Integration with Google maps  and Places API**
-   **Usage of navigation UI and bottom navigation view**

## Future improvements
-   **Add notifications for adding or changing orders**
-  **Support in pagination** 
## Setup

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/GalRadia/FeedForward_Organization
    ```
    
2.  **Install Dependencies**:  
    Ensure that Gradle is installed and your environment is set up for Android development. Run:
```bash
 ./gradlew build
 ```

3.  **Configure Secrets **:
-   `Secrets.properties`: Ensure this file contains your Google API key
    
-   `Local.defaults.properties`: Set up default configurations here.
```
MAPS_API_KEY=YOUR_API_KEY
```
## Application Flow

[](https://github.com/TzachiPinhas/Miniapp_Restaurant#application-flow)

1.  **Registration/Login**: Association begin by registering for an account using an email.
2.  **Home Page**: Upon successful login, the home page displays the restaurants in radius of 5,15,30 KM from the association
3.  **Managing Donations**: After picking a restaurant you can create an order from the restaurant's storage
4.  **My Orders**: Association can track all the orders (Pending, Delivered  and Active)
5.  **Reviews**: After finishing an order the association can add a review to the restaurant.
## Login Page
<img src="https://github.com/user-attachments/assets/bf8b7723-aefa-4341-828a-6a16f8baad4e" alt="Login Page" width="300" height="600">

## Pick Restaurant
<img src="https://github.com/user-attachments/assets/d56474bf-941a-4385-84f5-7bbca5f1a75a" alt="Pick Restaurant" width="300" height="600">

## Select food amound for order
<img src="https://github.com/user-attachments/assets/8b785f90-ce24-4dbf-a3bb-1f8a31a84dbc" alt="Select food amound for order" width="300" height="600">

## Check My Orders
<img src="https://github.com/user-attachments/assets/987420cf-d4c9-4ce9-bc18-cbd2bd511063" alt="Check My Orders" width="300" height="600">
<img src="https://github.com/user-attachments/assets/eca2649b-dbcd-4c39-b054-a4c78d3715d6" alt="Check My Orders" width="300" height="600">

