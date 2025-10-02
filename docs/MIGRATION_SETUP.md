# Quick Start: Database Migrations

> **Full Documentation**: See [DATABASE_MIGRATIONS.md](./DATABASE_MIGRATIONS.md) for complete guide  
> **Main Documentation**: [README.md](../README.md)

## 📋 Overview

This guide helps you get started with database migrations using Flyway in your Spring Boot application. Flyway automatically manages your database schema changes in a version-controlled, repeatable way.

**Related Documentation:**
- [Deployment Guide](./DEPLOYMENT.md) - For deployment configurations
- [Main README](../README.md) - Project overview

## ✅ What Was Done

1. **Added Flyway dependencies** to `pom.xml`:
   - `flyway-core`
   - `flyway-database-postgresql`

2. **Updated `application.properties`**:
   - Changed `spring.jpa.hibernate.ddl-auto` from `create` to `validate`
   - Added Flyway configuration
   - Flyway is now enabled and will auto-run on startup

3. **Created migration structure**:
   ```
   src/main/resources/db/migration/
   └── V1__create_users_table.sql
   ```

4. **First migration created** (`V1__create_users_table.sql`):
   - Creates the `users` table
   - Adds indexes for better performance
   - Matches your existing `User` entity

## 🚀 Next Steps for Render.com

### 1. Update Environment Variables

In your Render.com dashboard, set:

```bash
HIBERNATE_DDL_AUTO=validate
```

This tells Hibernate to only validate the schema (Flyway will manage it).

### 2. Deploy

When you deploy, Flyway will automatically:
1. Create the `flyway_schema_history` table
2. Run `V1__create_users_table.sql`
3. Create your `users` table
4. Track the migration in the history table

### 3. Verify

After deployment, check your logs for:
```
Flyway: Migrating schema "public" to version "1 - create users table"
Flyway: Successfully applied 1 migration
```

## 📝 Creating New Migrations

### Example: Add a new column

1. Create file: `src/main/resources/db/migration/V2__add_user_phone.sql`
   ```sql
   ALTER TABLE users ADD COLUMN phone VARCHAR(20);
   ```

2. Commit and push - Flyway will auto-apply on next deployment

### Example: Create a new table

1. Create file: `src/main/resources/db/migration/V3__create_posts_table.sql`
   ```sql
   CREATE TABLE posts (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       title VARCHAR(500) NOT NULL,
       content TEXT,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id)
   );
   ```

2. Commit and push

## 🔍 Important Notes

- ✅ Migrations run automatically on application startup
- ✅ Migrations run in order (V1, V2, V3, etc.)
- ✅ Flyway tracks which migrations have been applied
- ❌ **Never modify a migration after it's been deployed**
- ❌ **Never delete a migration file**

## 🐛 Fixing the Original Error

Your original error was:
```
Schema-validation: missing table [users]
```

This happened because:
- Hibernate was set to `validate` mode
- The `users` table didn't exist yet
- No migration tool was configured to create it

Now with Flyway:
- ✅ Migrations will create the table automatically
- ✅ Hibernate will validate it matches your entities
- ✅ Schema changes are version-controlled

## 📚 More Info

See `DATABASE_MIGRATIONS.md` for complete documentation.

---

**Ready to deploy!** Your database migrations are now properly configured. 🎉
