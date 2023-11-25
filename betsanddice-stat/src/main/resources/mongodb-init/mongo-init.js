db.createUser({
    user: "admin_stat",
    pwd: "XMOJocgKlmRHz2O",
    roles: [
      { role: "dbOwner", db: "stats" }
    ]
  });

//db.createCollection("user-stats");





