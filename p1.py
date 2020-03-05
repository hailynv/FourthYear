import csv
# Hailyn Valle Luque | ACO240 [MWF 2:00-2:50]

### STORM CLASS DEFINITION ###


class Storm:

    def __init__(self, i, o, s, y, n):
        self._id = i
        self._origin = o
        self._storm_num = s
        self._year = y
        self._name = n
        self._observations = []

    def add_observation(self, obs):
        self._observations.append(obs)

    def __repr__(self):
        return str((self._id, self._origin, self._storm_num,
                    self._year, self._name, len(self._observations)))

    def get_id(self):
        return self._id

    def get_origin(self):
        return self._origin

    def get_storm_num(self):
        return self._storm_num

    def get_year(self):
        return self._year

    def get_name(self):
        return self._name

    def get_observations(self):
        return self._observations


### OBSERVATION CLASS DEFINITON ###
class Observation:

    def __init__(self, s, d, t, st, lat, lon, mw, mp):
        self._storm = s
        self._date = d
        self._time = t
        self._status = st
        self._latitude = lat
        self._longitude = lon
        self._max_wind = mw
        self._min_pressure = mp

    def __repr__(self):
        return str((self._storm.get_name(), self._date, self._time, self._latitude,
                    self._longitude, self._max_wind, self._min_pressure))

    def get_storm(self):
        return self._storm

    def get_date(self):
        return self._date

    def get_time(self):
        return self._time

    def get_status(self):
        return self._status

    def get_latitude(self):
        return self._latitude

    def get_longitude(self):
        return self._longitude

    def get_max_wind(self):
        return self._max_wind

    def get_min_pressure(self):
        return self._min_pressure

### ORIGIN_YEAR_DICT ###


origin_year_dict = {}

with open('storms_three_years.csv', newline='') as storm_data:
    reader = csv.DictReader(storm_data)

    for row in reader:
        key = (row['ID'][0:2], int(row['ID'][4:8])) # generating the key

        if key not in origin_year_dict: # if key has not been added to the dictionary yet
            current_storm = Storm(row['ID'], row['ID'][0:2], # creating a storm
                                  int(row['ID'][2:4]), int(row['ID'][4:8]), row['Name'].strip())
            # adding an observation to the storm just created
            current_storm.add_observation(Observation(current_storm, int(row['Date']), int(row['Time']), row['Status'].strip(),
                                                      row['Latitude'], row['Longitude'], int(row['Maximum Wind']), int(row['Minimum Pressure'])))
            # adding the storm to the key's storm list
            origin_year_dict[key] = [current_storm]

        else:
            found = False
            for storm in origin_year_dict[key]: # iterating through list of storms
                if row['Name'].strip() == storm.get_name(): # if the storm is already in the list
                    # adding an observation to the storm found
                    storm.add_observation(Observation(storm, int(row['Date']), int(row['Time']), row['Status'].strip(),
                                                      row['Latitude'], row['Longitude'], int(row['Maximum Wind']), int(row['Minimum Pressure'])))
                    # storm has been found
                    found = True

            if not found: # if the storm is not in the list
                # create storm, add an observation, and add the storm to the key's storm list
                current_storm = Storm(row['ID'], row['ID'][0:2],
                                  int(row['ID'][2:4]), int(row['ID'][4:8]), row['Name'].strip())
                
                current_storm.add_observation(Observation(current_storm, int(row['Date']), int(row['Time']), row['Status'].strip(),
                                                      row['Latitude'], row['Longitude'], int(row['Maximum Wind']), int(row['Minimum Pressure'])))
                
                origin_year_dict[key].append(current_storm)

for entry in origin_year_dict:
    print(entry)
    print(origin_year_dict[entry])
    print()

### TESTER CODE ###

print("OUTPUT 1")
# creating a list of keys
sorted_keys = list(origin_year_dict.keys())
# sorting the keys by year and by the origin 
# t[1] = year | t[0] = origin
sorted_keys.sort(key=lambda t: (t[1], t[0]), reverse=True)  
for entry in sorted_keys: # iterating through sorted keys
    # getting all storm names from that specific tuple key
    storm_list = [storm.get_name() for storm in origin_year_dict[entry]]
    # sorting the storms by name
    storm_list.sort()
    # entry[1] = year | entry[0] = origin
    print(str(entry[1]) + " " + str(entry[0]) + " " +
          str(len(origin_year_dict[entry])) + " " + str(storm_list))


print()
print("OUTPUT 2")
# creating a set of unique years
year_set = set([entry[1] for entry in origin_year_dict.keys()])
# turning it to a list for sorting purposes 
year_list = [year for year in year_set]
year_list.sort(reverse=True)
# iterating through the years in the sorted year_list
for year in year_list:
    top_obs = [] # top observations for each year

    # storm list of storms in that particular year
    storm_list = [ 
        storm for entry in origin_year_dict for storm in origin_year_dict[entry] if storm.get_year() == year]

    print(str(year) + " " + str(len(storm_list)))

    for storm in storm_list:
        # finding the observation with the highest wind speed
        max_wind = max(storm.get_observations(),
                       key=lambda o: o.get_max_wind())

        # finding the observation with the lowest pressure
        min_pressure = min(storm.get_observations(),
                           key=lambda o: o.get_min_pressure())

        # adding a tuple with both observations to the list
        # of top observations
        top_obs.append((max_wind, min_pressure))

    # sorting the list of top observations by the max wind
    # o[0] = max_wind
    top_obs.sort(key=lambda o: o[0].get_max_wind(), reverse=True)

    for entry in top_obs:
        # entry[0] = max_ind | entry[1] = min_pressure
        print(entry[0].get_storm().get_name() + " " +
              str(entry[0].get_max_wind()) + " " + str(entry[1].get_min_pressure()))
    print()


### STATUS_DICT ###
status_dict = {}

# list of all storms ever
all_storms = [storm for entry in origin_year_dict for storm in origin_year_dict[entry]]
statuses = ['HU', 'TS', 'SS', 'TD', 'SD', 'EX', 'LO', 'WV', 'DB']
for status in statuses:
    status_dict[status] = [] # list for every status
    for storm in all_storms:
        # tuple with a storm object and a list of all observations with that status for that storm
        storm_tup = (storm, [obs for obs in storm.get_observations() if obs.get_status() == status])
        # if the list isn't empty, add it to the current status' list of tuples
        if len(storm_tup[1]) != 0:
            status_dict[status].append(storm_tup)

print("~~~~~~STATUS DICT~~~~~~")
for entry in status_dict:
    print(entry)
    print(status_dict[entry])
    print()
            
print("OUTPUT 3")
for entry in status_dict: # iterating through every status
    print(entry + " "  + str(len(status_dict[entry])))
    # sorting that key's list by storm name
    # t[0] = storm
    status_dict[entry].sort(key = lambda t: t[0].get_name()) 
    # iterating through (storm, [obs]) tuples in list for that key
    for tup in status_dict[entry]:
        # finding most recent observation and least recent observation
        # tup[1] = list of observations in (storm, [obs]) tuple
        most_recent = max(tup[1], key = lambda o: (o.get_date(), o.get_time()))
        least_recent = min(tup[1], key = lambda o: (o.get_date(), o.get_time()))
        # tup[0] = storm | tup[1] = list of observations in (storm, [obs]) tuple
        print(tup[0].get_name() + " " + str(len(tup[1])) + " " + str((most_recent.get_date(), 
                most_recent.get_time())) + " " + str((least_recent.get_date(), least_recent.get_time())))
    print()