db.createUser({
    user: "admin_craps",
    pwd: "XMOJocgKlmRHz2O",
    roles: [
      { role: "dbOwner", db: "craps" }
    ]
  });

//db.createCollection("crapsGames");
//db.createCollection("diceRolls");




