db.createUser({
    user: "admin_tutorial",
    pwd: "XMOJocgKlmRHz2O",
    roles: [
      { role: "dbOwner", db: "tutorials" }
    ]
  });

db.createCollection("game-tutorials");