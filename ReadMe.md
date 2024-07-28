This system handles drone activity.\
It uses the H2 in-memory database for persistence.

It has 4 entities.
1. Drone - contains information about each drone
2. Medication - contains information about the medication being loaded.
3. MedicationItem - This contains the type of medication to be loaded and in what quantity.
4. DroneLoad - This contains information about the current load on a drone i.e one or more MedicationItem.

The following assumptions were made
* A drone can be reloaded even when it is already loaded.
* Medications are standard and the only thing that varies while loading them to the drone is the quantity.
* Drones start with a weight limit of 500 each if not provided.

Things ignored
* Handling battery percentage reduction.
* Showing list of medications.

The project contains 3 json files in the main resources path.
* initial-drones.json - this file contains valid drone information that is used to populate the database.
* initial_medication.json - this file contains 3 medications that will be loaded as the system starts
* wrong-drone-list.json - this file contains invalid drone information that is used for unit testing.

When the system starts, the `InitialDataLoader` class loads the first 3 drones and medications.
All drone functionality happens in the `DroneService`
All Unit Tests are in the `DroneApisTest` inside the `test` directory.