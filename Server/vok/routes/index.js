// routes/index.js

module.exports = function(app, Models, fs, multer)
{
    // Check Whether Server is Alive
    app.get('/', function(req, res){
        console.log("request to /");
        res.render("index.html");
    })

   	app.get('/api', function(req, res){
   		console.log("request to /api");
   		res.render("list.html");
   	})

    // Download Image from Url Path
    app.get('/images/*', function(req, res){
        console.log("request to "+req.originalUrl);
        // console.log(__dirname)
        // console.log(req.protocol)
        // console.log(req.get('host'))
        // console.log(req.originalUrl)
        // console.log(req.protocol+"://"+req.get('host')+req.originalUrl)
        res.download(__dirname + '/..'+req.originalUrl)
    })

    // GET ALL ADMINS
    app.get('/api/admin', function(req,res){
        console.log("request to /api/admin")
        Models.Admin.find(function(err, admin){
            if(err) return res.status(500).send({error: 'database failure'});
            res.json(admin);
        })
    });

    // GET ALL BROADCASTS
    app.get('/api/broadcast', function(req,res){
        console.log("request to /api/broadcast")
        Models.Broadcast.find(function(err, broadcast){
            if(err) return res.status(500).send({error: 'database failure'});
            res.json(broadcast);
        })
    });

    // GET SINGLE ADMIN
    app.get('/api/admin/:email', function(req, res){
		console.log("request to /api/admin/" + req.params.email)
		Models.Admin.findOne({email: req.params.email}, function(err, admin){
			if (err) return res.status(500).json({error: err});
			if (!admin) return res.status(404).json({error: 'no such admin exists'});
			res.json(admin);
		})
	})

	// GET SINGLE BROADCAST
    app.get('/api/broadcast/:id', function(req, res){
		console.log("request to /api/broadcast/" + req.params.id)
		Models.Broadcast.findOne({id: req.params.id}, function(err, broadcast){
			if (err) return res.status(500).json({error: err});
			if (!broadcast) return res.status(404).json({error: 'no such broadcast exists'});
			res.json(broadcast);
		})
	})

    // CREATE ADMIN
    app.post('/api/addadmin', function(req, res){
        console.log("request to /api/addadmin")

        if (req.body.email == null){
            return res.status(400).json({error: 'email not found in request'});
        }

        Models.Admin.findOne({email: req.body.email}, function(err, admin){
            if (err) return res.status(500).json({error: err});
            if (admin) return res.status(406).json({error: 'admin already exists'});
            var admin = new Models.Admin();
            admin.email = req.body.email;
            admin.name = req.body.name;
            admin.job = req.body.job;
            admin.yearnumber = req.body.yearnumber;
            admin.password = req.body.password;

            admin.save(function(err){
                if(err){
                    console.error(err);
                    res.json({result: 0});
                    return;
                }

                res.json({result: 1});
            });
        });
    });

    // CREATE BROADCAST
    app.post('/api/addbroadcast', function(req, res){
        console.log("request to /api/addbroadcast")

        Models.Broadcast.findOne({id: req.body.id}, function(err, broadcast){
            if (err) return res.status(500).json({error: err});
            if (broadcast) return res.status(406).json({error: 'broadcast already exists'});
            var broadcast = new Models.Broadcast();
            broadcast.id = req.body.id;
            broadcast.title = req.body.title;
            broadcast.category = req.body.category;
            broadcast.day = req.body.day;
            broadcast.time = req.body.time;
            broadcast.producer = req.body.producer;
            broadcast.engineer = req.body.engineer;
            broadcast.anouncer = req.body.anouncer;
            broadcast.status = "off";

            var storage = multer.diskStorage({
                destination: function(req, file, cb){
                    cb(null, 'images')
                },
                filename: function(req, file, cb){
                    file.uploadFile = {
                        name: req.body.id,
                        ext: file.mimetype.split('/')[1]
                    }
                    cb(null, file.uploadFile.name+'.'+file.uploadFile.ext)
                }
            });

            var upload = multer({storage: storage}).single('thumbnail');

            upload(req, res, function(err){
                if (err) return res.status(500).send({error: err});

                if (req.file==null){
                    console.log(req.file);
                    broadcast.save(function(err){
                        if (err) {
                            console.log(err);
                            res.json({result: 0});
                            return;
                        }
                        res.json({result: 1});
                    });
                    return;
                }
                console.log(req.file);
                broadcast.thumbnail = 'images/'+req.file.uploadFile.name+'.'+req.file.uploadFile.ext;
                
                broadcast.save(function(err){
                    if (err) {
                        console.log(err);
                        res.json({result: 0});
                        return;
                    }
                    res.json({result: 1});

                });
            });
        });
    });

    // UPDATE THE ADMIN
    app.put('/api/admin/:email', function(req, res){
        console.log("[PUT]request to /api/admin/"+req.params.email);

        email = req.params.email;

        Models.Admin.findOne({email: email}, function(err, admin){
        	if (err) return res.status(500).send({error: err});	
			if (!admin) {
				return res.send({error: 'no such admin exists'});
			}

			admin.name = req.body.name;
            admin.password = req.body.password;

            admin.save(function(err){
            	if (err){
            		console.error(err);
            		res.json({result: 0});
            		return;
            	}

            	res.json({result: 1});
            });
        });
    });

    // UPDATE THE BROADCAST
    app.put('/api/broadcast/:id', function(req, res){
        console.log("[PUT]request to /api/broadcast/"+req.params.id);

        id = req.params.id;

        Models.Broadcast.findOne({id: id}, function(err, broadcast){
        	if (err) return res.status(500).send({error: err});	
			if (!broadcast) {
				return res.send({error: 'no such broadcast exists'});
			}

			broadcast.title = req.body.title;
            broadcast.category = req.body.category;
            broadcast.day = req.body.day;
            broadcast.time = req.body.time;
            broadcast.producer = req.body.producer;
            broadcast.engineer = req.body.engineer;
            broadcast.anouncer = req.body.anouncer;

            var storage = multer.diskStorage({
                destination: function(req, file, cb){
                    cb(null, 'images')
                },
                filename: function(req, file, cb){
                    file.uploadFile = {
                        name: req.body.id,
                        ext: file.mimetype.split('/')[1]
                    }
                    cb(null, file.uploadFile.name+'.'+file.uploadFile.ext)
                }
            });

            var upload = multer({storage: storage}).single('thumbnail');

            upload(req, res, function(err){
                if (err) return res.status(500).send({error: err});

                if (req.file==null){
                    console.log(req.file);
                    return res.send({error: 'no image attached'});
                }
                console.log(req.file);
                broadcast.thumbnail = 'images/'+req.file.uploadFile.name+'.'+req.file.uploadFile.ext;
                
                broadcast.save(function(err){
                    if (err) {
                        console.log(err);
                        res.json({result: 0});
                        return;
                    }
                    res.json({result: 1});

                });
            });
        });
    });

    // UPDATE THE BROADCAST IMAGE
    app.put('/api/uploadimage/:id', function(req, res){
        console.log("[PUT]request to /api/uploadimage/"+req.params.id);

        id = req.params.id;

        Models.Broadcast.findOne({id: id}, function(err, broadcast){
            if (err) return res.status(500).send({error: err}); 
            if (!broadcast) {
                return res.send({error: 'no such broadcast exists'});
            }

            var storage = multer.diskStorage({
                destination: function(req, file, cb){
                    cb(null, 'images')
                },
                filename: function(req, file, cb){
                    file.uploadFile = {
                        name: req.body.id,
                        ext: file.mimetype.split('/')[1]
                    }
                    cb(null, file.uploadFile.name+'.'+file.uploadFile.ext)
                }
            });

            var upload = multer({storage: storage}).single('thumbnail');

            upload(req, res, function(err){
                if (err) return res.status(500).send({error: err});

                if (req.file==null){
                    console.log(req.file);
                    return res.send({error: 'no image attached'});
                }
                console.log(req.file);
                broadcast.thumbnail = 'images/'+req.file.uploadFile.name+'.'+req.file.uploadFile.ext;
                
                broadcast.save(function(err){
                    if (err) {
                        console.log(err);
                        res.json({result: 0});
                        return;
                    }
                    res.json({result: 1});

                });
            });
        });
    });

    // UPDATE THE BROADCAST STATUS
    app.put('/api/onair/:id', function(req, res){
        console.log("[PUT]request to /api/onair/"+req.params.id);

        id = req.params.id;

        Models.Broadcast.findOne({id: id}, function(err, broadcast){
            if (err) return res.status(500).send({error: err}); 
            if (!broadcast) {
                return res.send({error: 'no such broadcast exists'});
            }

            broadcast.status = req.body.status;
            broadcast.songs = req.body.songs;
            console.log(req.body.status);

            broadcast.save(function(err){
                if (err){
                    console.error(err);
                    res.json({result: 0});
                    return;
                }

                res.json({result: 1});
            });
        });
    });

    // DELETE ADMIN
    app.delete('/api/admin/:email', function(req, res){
        Models.Admin.remove({email: req.params.email}, function(err, output){
	        if(err) return res.status(500).json({ error: "database failure" });
	        res.status(204).end();
    	})
    });

    // DELETE BROADCAST
    app.delete('/api/broadcast/:id', function(req, res){
        Models.Broadcast.remove({id: req.params.id}, function(err, output){
            if(err) return res.status(500).json({ error: "database failure" });
            res.status(204).end();
        })
    });

}