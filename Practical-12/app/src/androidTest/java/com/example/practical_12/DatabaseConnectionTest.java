package com.example.practical_12;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseConnectionTest {

    private DatabaseManager dbManager;
    private Context context;

    @Before
    public void setUp() {
        // Get application context
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        // Initialize database manager
        dbManager = new DatabaseManager(context);
        dbManager.open();
        
        // Clear all existing data before each test
        dbManager.deleteAllUsers();
    }

    @Test
    public void testDatabaseConnection() {
        // Test that database is connected
        assertNotNull(dbManager);
    }

    @Test
    public void testInsertUser() {
        // Test inserting a single user
        long userId = dbManager.insertUser("John Doe", "john@example.com", 30);
        
        // Verify user was inserted (id should be > 0)
        assertTrue("User insertion failed", userId > 0);
        
        // Verify user count increased
        int count = dbManager.getUserCount();
        assertEquals("User count should be 1", 1, count);
    }

    @Test
    public void testInsertMultipleUsers() {
        // Insert multiple users
        dbManager.insertUser("John Doe", "john@example.com", 30);
        dbManager.insertUser("Jane Smith", "jane@example.com", 28);
        dbManager.insertUser("Bob Johnson", "bob@example.com", 35);
        
        // Verify all users were inserted
        int count = dbManager.getUserCount();
        assertEquals("Should have 3 users", 3, count);
    }

    @Test
    public void testRetrieveAllUsers() {
        // Insert test data
        dbManager.insertUser("User1", "user1@example.com", 25);
        dbManager.insertUser("User2", "user2@example.com", 30);
        dbManager.insertUser("User3", "user3@example.com", 35);
        
        // Retrieve all users
        List<User> users = dbManager.getAllUsers();
        
        // Verify users were retrieved
        assertEquals("Should retrieve 3 users", 3, users.size());
        
        // Verify user data
        User firstUser = users.get(0);
        assertNotNull("User should not be null", firstUser);
        assertEquals("User name should match", "User3", firstUser.getName());
    }

    @Test
    public void testRetrieveUserById() {
        // Insert a user
        long userId = dbManager.insertUser("Test User", "test@example.com", 40);
        
        // Retrieve user by ID
        User user = dbManager.getUserById((int) userId);
        
        // Verify user data
        assertNotNull("User should be found", user);
        assertEquals("Name should match", "Test User", user.getName());
        assertEquals("Email should match", "test@example.com", user.getEmail());
        assertEquals("Age should match", 40, user.getAge());
    }

    @Test
    public void testUpdateUser() {
        // Insert a user
        long userId = dbManager.insertUser("Old Name", "old@example.com", 25);
        
        // Update user
        int updateCount = dbManager.updateUser((int) userId, "New Name", "new@example.com", 30);
        
        // Verify update was successful
        assertEquals("Update should affect 1 row", 1, updateCount);
        
        // Retrieve and verify updated user
        User user = dbManager.getUserById((int) userId);
        assertEquals("Name should be updated", "New Name", user.getName());
        assertEquals("Email should be updated", "new@example.com", user.getEmail());
        assertEquals("Age should be updated", 30, user.getAge());
    }

    @Test
    public void testDeleteUser() {
        // Insert a user
        long userId = dbManager.insertUser("To Delete", "delete@example.com", 50);
        
        // Delete user
        int deleteCount = dbManager.deleteUser((int) userId);
        
        // Verify deletion was successful
        assertEquals("Delete should affect 1 row", 1, deleteCount);
        
        // Verify user was deleted
        User user = dbManager.getUserById((int) userId);
        assertNull("User should be null after deletion", user);
    }

    @Test
    public void testGetUserCount() {
        // Initial count should be 0
        int initialCount = dbManager.getUserCount();
        assertEquals("Initial count should be 0", 0, initialCount);
        
        // Add users and verify count
        dbManager.insertUser("User1", "user1@example.com", 25);
        int count1 = dbManager.getUserCount();
        assertEquals("Count should be 1", 1, count1);
        
        dbManager.insertUser("User2", "user2@example.com", 30);
        int count2 = dbManager.getUserCount();
        assertEquals("Count should be 2", 2, count2);
    }

    @Test
    public void testDeleteAllUsers() {
        // Insert multiple users
        dbManager.insertUser("User1", "user1@example.com", 25);
        dbManager.insertUser("User2", "user2@example.com", 30);
        dbManager.insertUser("User3", "user3@example.com", 35);
        
        // Delete all users
        dbManager.deleteAllUsers();
        
        // Verify all users were deleted
        int count = dbManager.getUserCount();
        assertEquals("All users should be deleted", 0, count);
        
        List<User> users = dbManager.getAllUsers();
        assertTrue("User list should be empty", users.isEmpty());
    }

    @Test
    public void testUserDataPersistence() {
        // Insert user
        long userId = dbManager.insertUser("Persistent User", "persistent@example.com", 45);
        
        // Close and reopen database
        dbManager.close();
        dbManager = new DatabaseManager(context);
        dbManager.open();
        
        // Verify user still exists
        User user = dbManager.getUserById((int) userId);
        assertNotNull("User should persist after reopening database", user);
        assertEquals("Persisted user name should match", "Persistent User", user.getName());
    }
}
