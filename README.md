# insuranceApp
insurance app


*logical story
 a client can enter the website sign up as new user if not exist,
 then he can sign in,then he can choose what operation he like to do:
 add new produc:if he likes to add a new product the site would return hil the product offering and he will be able to add to his order;
 check witch products he has: then the site would return his owned products;
 then also he can choose to update an existing product,by chooing the product:th client might 
 extand his flight insurance expiration date and effective date if flight change for example.
 
 
 then he can choose an insurence plane tha fits for him based on the plans that exposed to him on the web site.
 after the client has an inure

*insurence app "erd" is attached in "submition" folder. 
*regarding the question what is missing logicaly in design and instructions:
from my prespective i think there is missing the pull product offering from catalog to know what offers you can buy.
if you create a new user he will defently not have any offers so tring reutrn him existing offers by id is useless ,
so he will directly need to get the product offerings that he can buy ,same for update product,should be enabled only for existing customer with an existing orders and products.


explanation regarding app:
like described in the swagger atached there are api's for :
*client creation
*authentication&authorization
*buy new product {enabls to choose few product for the client by product id }
*update existing product by product id
get owned products by client id 
