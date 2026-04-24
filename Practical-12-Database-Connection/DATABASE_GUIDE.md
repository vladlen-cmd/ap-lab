# SQLite Database Integration Guide

## Overview
This document describes the SQLite database implementation for the Android application. The database stores user information and provides full CRUD (Create, Read, Update, Delete) operations.

## Database Structure

### Database Name
- `practical_db` (stored in app's private directory)

### Table: `users`
Stores user information with the following columns:

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique user identifier |
| name | TEXT | NOT NULL | User's full name |
| email | TEXT | NOT NULL | User's email address |
| age | INTEGER | | User's age |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

## Components

### 1. DatabaseHelper.java
**Purpose**: Manages SQLite database creation and versioning

**Key Methods**:
- `onCreate(SQLiteDatabase db)`: Creates tables when database is first created
- `onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)`: Handles database schema upgrades
- `dropAllTables(SQLiteDatabase db)`: Clears all tables (useful for testing)

**Usage**:
```java
DatabaseHelper helper = new DatabaseHelper(context);
SQLiteDatabase db = helper.getWritableDatabase();
```

### 2. User.java
**Purpose**: Model class representing a user record

**Constructors**:
- `User(int id, String name, String email, int age, String createdAt)`: Full constructor
- `User(String name, String email, int age)`: Constructor for new records (without ID)

**Key Methods**:
- Getters: `getId()`, `getName()`, `getEmail()`, `getAge()`, `getCreatedAt()`
- Setters: `setId()`, `setName()`, `setEmail()`, `setAge()`, `setCreatedAt()`
- `toString()`: Returns formatted string representation

### 3. DatabaseManager.java
**Purpose**: Provides high-level database operations

**Key Methods**:

#### Connection Management
```java
public void open()          // Open database connection
public void close()         // Close database connection
```

#### Create Operations
```java
// Insert user with parameters
public long insertUser(String name, String email, int age)

// Insert User object
public long insertUser(User user)
```

**Returns**: Long - row ID of inserted record (-1 if failed)

#### Read Operations
```java
// Get all users
public List<User> getAllUsers()

// Get user by ID
public User getUserById(int id)

// Get total user count
public int getUserCount()
```

#### Update Operations
```java
public int updateUser(int id, String name, String email, int age)
```

**Returns**: Integer - number of rows updated

#### Delete Operations
```java
// Delete user by ID
public int deleteUser(int id)

// Delete all users
public void deleteAllUsers()
```

## Implementation in Activities/Fragments

### In Fragment onCreate/onCreateView:
```java
private DatabaseManager dbManager;

@Override
public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    // Initialize database manager
    dbManager = new DatabaseManager(requireContext());
    dbManager.open();
    
    // Set up UI listeners...
}
```

### Inserting Data:
```java
long userId = dbManager.insertUser("John Doe", "john@example.com", 30);
if (userId > 0) {
    Toast.makeText(context, "User inserted: " + userId, Toast.LENGTH_SHORT).show();
} else {
    Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show();
}
```

### Retrieving Data:
```java
// Get all users
List<User> users = dbManager.getAllUsers();
for (User user : users) {
    Log.d("User", user.toString());
}

// Get specific user
User user = dbManager.getUserById(1);
if (user != null) {
    Log.d("User", "Name: " + user.getName());
}
```

### Updating Data:
```java
int updatedRows = dbManager.updateUser(1, "Jane Doe", "jane@example.com", 28);
if (updatedRows > 0) {
    Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show();
}
```

### Deleting Data:
```java
int deletedRows = dbManager.deleteUser(1);
if (deletedRows > 0) {
    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
}
```

### In Fragment onDestroyView:
```java
@Override
public void onDestroyView() {
    super.onDestroyView();
    if (dbManager != null) {
        dbManager.close();
    }
    binding = null;
}
```

## Testing

### Running Tests
The project includes comprehensive unit tests in `DatabaseConnectionTest.java` to verify database functionality:

```bash
# Run instrumented tests (Android device/emulator required)
./gradlew connectedAndroidTest
```

### Test Coverage
- `testDatabaseConnection()`: Verifies database connection
- `testInsertUser()`: Tests single user insertion
- `testInsertMultipleUsers()`: Tests multiple insertions
- `testRetrieveAllUsers()`: Tests retrieving all records
- `testRetrieveUserById()`: Tests retrieving specific record
- `testUpdateUser()`: Tests updating records
- `testDeleteUser()`: Tests deleting records
- `testGetUserCount()`: Tests record counting
- `testDeleteAllUsers()`: Tests bulk deletion
- `testUserDataPersistence()`: Tests data persistence across connections

## File Structure
```
app/src/
├── main/
│   ├── java/com/example/practical_12/
│   │   ├── DatabaseHelper.java
│   │   ├── DatabaseManager.java
│   │   ├── User.java
│   │   ├── FirstFragment.java (Insert UI)
│   │   ├── SecondFragment.java (View/Delete UI)
│   │   └── MainActivity.java
│   └── res/layout/
│       ├── fragment_first.xml
│       └── fragment_second.xml
└── androidTest/
    └── java/com/example/practical_12/
        └── DatabaseConnectionTest.java
```

## Database File Location
The SQLite database file is stored at:
- **Path**: `/data/data/com.example.practical_12/databases/practical_db`
- **Accessible via**: Android Device File Explorer (Android Studio) or `adb shell`

### Viewing Database (via ADB):
```bash
# Connect to device shell
adb shell

# Access database
sqlite3 /data/data/com.example.practical_12/databases/practical_db

# View tables
.tables

# Query users
SELECT * FROM users;

# Exit
.quit
```

## Best Practices

1. **Always close the database**:
   ```java
   @Override
   public void onDestroy() {
       super.onDestroy();
       if (dbManager != null) {
           dbManager.close();
       }
   }
   ```

2. **Handle exceptions**:
   ```java
   try {
       long userId = dbManager.insertUser(name, email, age);
   } catch (Exception e) {
       Log.e("Database", "Error inserting user", e);
   }
   ```

3. **Use parameterized queries** (already implemented in DatabaseManager):
   - Prevents SQL injection attacks
   - Handles special characters properly

4. **Check for null values**:
   ```java
   User user = dbManager.getUserById(1);
   if (user != null) {
       // Use user data
   }
   ```

5. **Use background threads for large operations**:
   ```java
   new Thread(() -> {
       List<User> users = dbManager.getAllUsers();
       // Update UI on main thread
       runOnUiThread(() -> updateUI(users));
   }).start();
   ```

## Common Issues & Solutions

### Issue: Database locked
**Solution**: Ensure database is properly closed in onDestroy/onDestroyView

### Issue: Data not persisting
**Solution**: Call `dbManager.open()` before operations and `dbManager.close()` after

### Issue: App crashes on empty query
**Solution**: Always check if cursors and lists are empty before accessing:
```java
List<User> users = dbManager.getAllUsers();
if (!users.isEmpty()) {
    User firstUser = users.get(0);
}
```

## Future Enhancements

1. Add data encryption (SQLCipher)
2. Implement data backup and restore
3. Add database migration system
4. Add transaction support
5. Implement Room persistence library
6. Add data synchronization with cloud database

## References
- [Android SQLite Documentation](https://developer.android.com/reference/android/database/sqlite)
- [SQLiteOpenHelper Guide](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper)
- [ContentValues Usage](https://developer.android.com/reference/android/content/ContentValues)
