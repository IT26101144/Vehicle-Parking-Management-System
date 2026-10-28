# Validation notes for this delivered build

- All Java source files were syntax-compiled with `javac` against minimal local stubs for the Spring annotations and `Model` API. This checks Java syntax and type consistency within the project source, but is not a replacement for a real Maven/Spring build.
- A local service-level harness passed: parking slot creation, vehicle entry automatically allocating a slot, vehicle exit releasing the slot, payment fee calculation for 2.5 hours at Rs. 100/hour (Rs. 300), and report record counts.
- A local Quick Sort harness passed for sorting report records by vehicle number and parking duration.
- HTML templates were parsed with Python's HTML parser without malformed HTML errors.
- A full Maven compile and Spring Boot startup could not be run in the build environment because Maven is not installed and the Maven wrapper could not download its distribution (network fetch failed). Please run `.\mvnw.cmd compile` and `.\mvnw.cmd spring-boot:run` on a computer with internet access before submitting.

## Manual acceptance checklist
1. Open `http://localhost:8081` and confirm the dashboard loads.
2. Add a vehicle, then edit and delete it.
3. Add a parking slot. Register a vehicle entry and verify a slot becomes occupied.
4. Record the active entry's exit and verify its slot becomes available.
5. Record a payment for a positive duration and check the calculated fee.
6. Open reports and verify both Quick Sort options.
7. Add, edit, and delete a user with dummy credentials only.

The parking fee is an example rate (Rs. 100/hour); change it if the lecturer specifies a different rate. This is an academic file-based application, not production-ready authentication or payment processing.
