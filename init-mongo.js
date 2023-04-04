db.createUser(
    {
        user: "lukasz",
        pwd: "lukasz1986",
        roles: [
            {
                role: "readWrite",
                db: "lotto-web"
            }
        ]
    }
)