# SQLite Database - Quick Start Guide

## What's Implemented

Your Android application now has a complete SQLite database system with:

✅ **Database Creation** - Automatic table creation on first run  
✅ **Insert Operations** - Add new user records  
✅ **Read Operations** - Retrieve all users or specific users  
✅ **Update Operations** - Modify existing records  
✅ **Delete Operations** - Remove individual or all records  
✅ **User Interface** - Two-fragment app for testing  
✅ **Comprehensive Tests** - Automated test suite for verification  

## Files Created/Modified

### New Files (Database Core):
- `DatabaseHelper.java` - Database schema and creation
- `User.java` - User data model
- `DatabaseManager.java` - Database operations layer

### New Files (Testing):
- `DatabaseConnectionTest.java` - Automated test suite
- `DATABASE_GUIDE.md` - Complete documentation

### Modified Files (UI Integration):
- `FirstFragment.java` - User insertion interface
- `SecondFragment.java` - User viewing interface
- `fragment_first.xml` - Insert form layout
- `fragment_second.xml` - View users layout

## Quick Test

### 1. Build and Run
```bash
./gradlew build
# Then run on emulator/device
```

### 2. Test in App
1. Open FirstFragment (Insert User tab)
2. Enter Name: "John Doe"
3. Enter Email: "john@example.com"
4. Enter Age: "30"
5. Click "Insert User"
6. Click "View All Users (Next)"
7. See inserted user in SecondFragment

### 3. Run Automated Tests
```bash
./gradlew connectedAndroidTest
```

## Code Examples

### Initialize Database in Fragment
```java
DatabaseManager dbManager = new DatabaseManager(requireContext());
dbManager.open();
```

### Insert User
```java
long userId = dbManager.insertUser("John", "john@email.com", 30);
System.out.println("Inserted user ID: " + userId);
```

### Get All Users
```java
List<User> users = dbManager.getAllUsers();
for (User user : users) {
    System.out.println(user.toString());
}
```

### Get Specific User
```java
User user = dbManager.getUserById(1);
if (user != null) {
    System.out.println("Name: " + user.getName());
    System.out.println("Email: " + user.getEmail());
}
```

### Update User
```java
dbManager.updateUser(1, "Jane Doe", "jane@email.com", 28);
```

### Delete User
```java
dbManager.deleteUser(1);
```

### Close Database
```java
dbManager.close();
```

## Database Schema

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Testing Checklist

- [ ] Insert user and verify in app
- [ ] Insert multiple users and view all
- [ ] Refresh view to see all users
- [ ] Delete all users
- [ ] Run automated tests with `./gradlew connectedAndroidTest`
- [ ] Check database file in Device File Explorer
- [ ] Restart app and verify data persists

## Database File Locations

### App Data
- `/data/data/com.example.practical_12/databases/practical_db`

### Access via ADB
```bash
adb shell sqlite3 /data/data/com.example.practical_12/databases/practical_db
sqlite> SELECT * FROM users;
sqlite> .quit
```

## Features

### Database Manager Features
- **Thread-safe cursor operations** - Properly closes cursors
- **Null-safe queries** - Handles empty result sets
- **Transaction support ready** - Can be extended
- **Parameterized queries** - Prevents SQL injection
- **Automatic timestamps** - Records when entries are created

### Fragment Features
- **Real-time input validation** - Checks for empty fields
- **Visual feedback** - Toast messages and text updates
- **User-friendly formatting** - Displays data clearly
- **Delete functionality** - Remove all records
- **Refresh capability** - Manual data reload

## Next Steps

1. **Test the app** - Run it and insert/view data
2. **Run automated tests** - Verify database works correctly
3. **Explore the code** - Understand the implementation
4. **Extend functionality** - Add more database operations
5. **Read DATABASE_GUIDE.md** - Complete documentation

## Troubleshooting

### App Crashes
- Ensure database is opened before operations: `dbManager.open()`
- Ensure database is closed in `onDestroyView()`: `dbManager.close()`

### Data Not Appearing
- Click "Refresh" button on Second Fragment
- Verify data was inserted (check toast message)
- Check logcat for errors

### Tests Fail
- Ensure emulator/device is running
- Run: `./gradlew connectedAndroidTest`
- Check Android Studio Logcat for error details

## Support for Further Development

The implementation is ready for:
- Adding search functionality
- Implementing data export (CSV/JSON)
- Adding data filtering and sorting
- Integrating with backend API
- Implementing user authentication
- Adding data synchronization

---

For detailed information, see **DATABASE_GUIDE.md**
