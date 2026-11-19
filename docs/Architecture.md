# Architecture
Main -> PowerBankService -> BST / Queue / Linked Lists / Sorting.
Models contain record data only. Each data-structure package owns its node and operations. Keep this standalone module independent of Spring Boot and MySQL; connect it to the larger application later through an adapter/service layer.
