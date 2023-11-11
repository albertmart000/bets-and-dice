db.createUser({
    user: "admin_stat",
    pwd: "XMOJocgKlmRHz2O",
    roles: [
      { role: "dbOwner", db: "stat" }
    ]
  });

db.createCollection("userStats");





