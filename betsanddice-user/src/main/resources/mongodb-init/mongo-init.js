db.createUser({
    user: "admin_user",
    pwd: "LuckyLuc000",
    roles: [
      { role: "dbOwner", db: "users" }
    ]
  });
db.createCollection("users");





