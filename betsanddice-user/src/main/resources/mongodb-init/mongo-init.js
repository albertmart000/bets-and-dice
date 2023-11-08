db.createUser({
    user: "admin_user",
    pwd: "XMOJocgKlmRHz2O",
    roles: [
      { role: "dbOwner", db: "users" }
    ]
  });
db.createCollection("users");





