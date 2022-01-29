#!/bin/bash

mongoimport --upsert --db WidgetDb --collection widgets ./scripts/widgets.json --jsonArray