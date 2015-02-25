# Sample application

This sample application is a very basic note taking application.
It consists of two modules:

 - `app`: Contains the application itself, with presenters and views;
 - `business`: Contains all business logic, such as creating and persisting notes.
 
## The application

The application is both presented with and without Dagger 2.0. The `plain` product flavor shows how you can use Triad without using Dagger, whereas the `dagger` product flavor does use Dagger. The common code, such as `Presenters`, `Containers` and `Views`, are in the `main` source folder.
