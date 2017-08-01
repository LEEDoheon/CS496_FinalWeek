// models/schema.js
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var adminSchema = new Schema({
    email: String, // primary key
    name: String,
    job: String,
    yearnumber: String,
    password: String
}, {versionKey: false});

var broadcastSchema = new Schema({
	id: String, // primary key
	title: String,
	category: String,
	day: String,
	time: String,
	thumbnail: String,
	producer: Array,
	engineer: Array,
	anouncer: Array,
	songs: Array,
	status: String
}, {versionKey: false});

module.exports = {
	Admin: mongoose.model('admin', adminSchema),
	Broadcast: mongoose.model('broadcast', broadcastSchema)
};
