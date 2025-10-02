# Database Migrations Guide

This project uses **Flyway** for database schema version control and migrations.

> **Quick Reference**: See [MIGRATION_SETUP.md](./MIGRATION_SETUP.md) for quick start guide  
> **Main Documentation**: [README.md](../README.md)

## 📚 Resources

- [Flyway Official Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
- [Flyway Maven Plugin](https://flywaydb.org/documentation/usage/maven/)
- [Database Best Practices](https://www.postgresql.org/docs/current/ddl.html)

## 📁 Migration Files Location

All migration files are stored in:
```
src/main/resources/db/migration/
```

## 📝 Naming Convention

Migration files must follow this strict naming pattern:
```
V{version}__{description}.sql
```

**Examples:**
- `V1__create_users_table.sql`
- `V2__add_user_role_column.sql`
- `V3__create_posts_table.sql`

**Important:**
- `V` must be uppercase
- Version numbers are sequential: V1, V2, V3, etc.
- Use **two underscores** (`__`) between version and description
- Use underscores (`_`) in the description instead of spaces

## 🚀 How to Create a New Migration

### Using Makefile (Easiest)

```bash
make migrate-create name=add_user_phone
```

This will automatically:
- Determine the next version number
- Create the file with proper naming: `V{N}__add_user_phone.sql`
- Add helpful header comments
- Place it in the correct directory

### Manual Creation

1. Create file: `src/main/resources/db/migration/V2__add_user_phone.sql`

2. Write your SQL migration:

```sql
-- V2__add_user_phone.sql
ALTER TABLE users ADD COLUMN phone VARCHAR(20);
```

3. Run migrations:

```bash
make migrate
# or
./mvnw spring-boot:run
```

## 🔍 Migration Examples

### Creating a Table
```sql
-- V1__create_users_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
```

### Adding a Column
```sql
-- V2__add_user_status.sql
ALTER TABLE users ADD COLUMN status VARCHAR(50) DEFAULT 'active';
```

### Creating a New Table with Foreign Key
```sql
-- V3__create_posts_table.sql
CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_posts_user_id ON posts(user_id);
```

### Inserting Seed Data
```sql
-- V4__seed_initial_users.sql
INSERT INTO users (username, email) VALUES 
    ('admin', 'admin@example.com'),
    ('testuser', 'test@example.com');
```

## ⚙️ Configuration

Current Flyway configuration in `application.properties`:

```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# JPA Configuration (important!)
spring.jpa.hibernate.ddl-auto=validate
```

**Note:** When using Flyway, set `spring.jpa.hibernate.ddl-auto` to `validate` or `none` to prevent Hibernate from modifying the schema.

## 🌍 Environment-Specific Settings

### For Render.com Deployment

Set these environment variables:
```bash
HIBERNATE_DDL_AUTO=validate
DATABASE_URL=<your-postgres-connection-string>
```

### For Local Development

Your local database URL is configured in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hello_spring
```

## 🔧 Flyway Commands (via Maven)

You can run Flyway commands using Maven directly or using the convenient Makefile shortcuts.

### Using Makefile (Recommended)

The Makefile provides easy-to-use commands for common migration tasks:

```bash
# Run all pending migrations
make migrate

# Show migration status and history
make migrate-info

# Validate migrations
make migrate-validate

# Create a new migration file
make migrate-create name=add_user_phone

# Show detailed migration status
make migrate-status

# Baseline existing database (for legacy databases)
make migrate-baseline

# Repair migration history (use with caution!)
make migrate-repair

# Clean database - ⚠️ DANGER: Deletes all data!
make migrate-clean
```

### Using Maven Directly

```bash
# Run migrations
./mvnw flyway:migrate

# Check migration status
./mvnw flyway:info

# Validate migrations
./mvnw flyway:validate

# Baseline existing database
./mvnw flyway:baseline

# Repair migration history
./mvnw flyway:repair

# Clean database (⚠️ DANGER: Deletes all data)
./mvnw flyway:clean
```

### Command Details

| Command | Description | Safe for Production? |
|---------|-------------|---------------------|
| `make migrate` | Runs all pending migrations | ✅ Yes |
| `make migrate-info` | Shows which migrations have been applied | ✅ Yes |
| `make migrate-validate` | Validates applied migrations | ✅ Yes |
| `make migrate-create` | Creates a new migration file with proper naming | ✅ Yes |
| `make migrate-status` | Shows detailed database and migration status | ✅ Yes |
| `make migrate-baseline` | Baselines existing database at current version | ⚠️ Use carefully |
| `make migrate-repair` | Repairs migration history (fixes checksums) | ⚠️ Use carefully |
| `make migrate-clean` | **Drops all objects in database** | ❌ Never in production! |

## 📊 Migration History

Flyway tracks all executed migrations in a special table called `flyway_schema_history`. This table is automatically created in your database.

To view migration history:
```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

## ⚠️ Important Rules

1. **Never modify a migration file after it has been applied** - Create a new migration instead
2. **Always test migrations locally first** before deploying
3. **Migrations are irreversible by default** - Plan carefully or create rollback migrations
4. **Keep migrations small and focused** - One logical change per migration
5. **Migrations run in order** - Ensure proper sequencing

## 🐛 Troubleshooting

### Error: "Schema-validation: missing table"

**Solution:** Set `HIBERNATE_DDL_AUTO=validate` and ensure migrations have been run.

### Error: "Validate failed: Detected resolved migration not applied"

**Solution:** Run `./mvnw flyway:migrate` or restart your application.

### Error: "Checksum mismatch"

**Cause:** A previously applied migration file was modified.

**Solution:** 
1. Never modify applied migrations
2. If absolutely necessary, use `./mvnw flyway:repair` (use with caution)

## 📚 Additional Resources

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
