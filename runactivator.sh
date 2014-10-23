 #!/bin/bash

./activator -jvm-debug 9999 -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url="jdbc:postgresql://localhost:5432/prm" -Ddb.default.user=navtk -Ddb.default.password=pass run
