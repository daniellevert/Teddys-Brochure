const mongoose = require('mongoose');
const express = require('express');

const nationalParkRouter = express.Router();
const ParkModel = mongoose.model('ParkModel');

nationalParkRouter.use((req, res, next) => {
	next();
});

nationalParkRouter.get('/', async (req, res) => {
	const park = await ParkModel.find();
	res.status(200).send(park);
});

nationalParkRouter.get('/:id', async (req, res) => {
	const parkId = req.params.id;

	if (parkId == null) {
		res.status(404).send({ error: 'No Park Is Found' });
		return;
	}

	let park;
	try {
		if (mongoose.Types.ObjectId.isValid(parkId)) {
			park = await ParkModel.find({ _id: parkId });
		} else {
			park = await ParkModel.find({ parkName: parkId });
		}
	} catch (e) {
		res.status(404).send({ error: 'No Park Found' });
	}

	if (park.length === 0) {
		res.status(404).send({ error: 'No Park Found' });
		return;
	}

	res.status(200).send(park);
});

nationalParkRouter.post('/', async (req, res) => {
	const parkInfo = req.body;
	const {
		parkName, blurb, latdec,
		longdec, map, images,
	} = parkInfo;

	if (parkInfo == null) {
		res.status(404).send({ error: 'No Park Is Found' });
		return;
	}

	const park = new ParkModel({
		parkName,
		blurb,
		latdec,
		longdec,
		map,
		images,
	});

	try {
		await park.save();
	} catch (e) {
		res.status(404).send({ error: 'Back end issues' });
	}

	res.status(200).send(park);
});

nationalParkRouter.put('/:id', async (req, res) => {
	const {
		blurb, latdec,
		longdec, images,
	} = req.body;

	const parkId = req.params.id;

	if (parkId == null) {
		res.status(404).send({ error: 'No Park Found' });
		return;
	}

	if (!mongoose.Types.ObjectId.isValid(parkId)) {
		res.status(404).send({ error: 'No Park Found' });
		return;
	}

	if (blurb == null || latdec == null || longdec == null || images == null) {
		res.status(404).send({ error: 'Make sure blurb, latdec, longdec, and images are filled' });
		return;
	}

	await ParkModel.updateOne({ _id: parkId }, {
		blurb,
		latdec,
		longdec,
		$push: { images },
	});

	res.status(200).send({ status: 'success' });
});

module.exports = nationalParkRouter;
