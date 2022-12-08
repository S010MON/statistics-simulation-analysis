%% Loading Data
data_log = readtable("log.txt");
% display(data_log); % For testing

time = []; % Time_logs
number = []; % Number_logs
event = []; % Event_logs

% length variable of the data 
length = size(data_log);
length = length(1);


for i = 1 : length
number = [number,table2array(data_log(i,1))];
time = [time,table2array(data_log(i,2))];
event = [event,table2array(data_log(i,3))];
end

% Cleaning the number and time arrays of NaN numbers that were generated due to headers
index_to_delete_number = [];
index_to_delete_time = [];
index_to_delete_event = [];

% Checking where NaN are and storing their indices.
for i = 1 : length
    if isnan(number(i))
        index_to_delete_number = [index_to_delete_number,i];
    end

    if isnan(time(i))
        index_to_delete_time = [index_to_delete_time,i];
    end

    if strcmp(event(i),'event')
        index_to_delete_event = [index_to_delete_event,i];
    end 
end

% Deleting 
for i = 1 : numel(index_to_delete_number)
    index = index_to_delete_number(i);
    number(index) = [];
end

for i = 1 : numel(index_to_delete_time)
    index = index_to_delete_time(i);
    time(index) = [];
end

for i = 1 : numel(index_to_delete_event)
    index = index_to_delete_event(i);
    event(index) = [];
end

% Updating length
new_length = size(number);
length = new_length(2);

%% Computing matrix of waiting times with respect to numbers and events : time_sum_per_number

% Computing sum of times with respect to number
% When number appears 3 times in a row : patient pops up - ambulance dispatched - Goes to hospital
% When number appears 2 times in a row : ambulance travels back to hub - hub reached
% Encoding Priority as the following : 
% 0 - No data
% 1 - A1 priority
A1 = 'New patient priority A1';
% 2 - A2 priority
A2 = 'New patient priority A2';
% 3 - B priority
B = 'New patient priority B';

% Building matrix
time_sum_per_number = zeros(max(number),2);

% Building priority - THIS WORKS apart from the 0.0001 - 0.0002 - 0.0003 I get instead of 1-2-3, you can display(time_sum_per_number)
for i = 1 : length
    index_prio = number(i);

    if strcmp(event(i),A1)
            if time_sum_per_number(index_prio,2) == 0
                time_sum_per_number(index_prio,2) = 1.0;
            end
    end

    if strcmp(event(i),A2)
            if time_sum_per_number(index_prio,2) == 0
                time_sum_per_number(index_prio,2) = 2.0;
            end
    end

    if strcmp(event(i),B)
            if time_sum_per_number(index_prio,2) == 0
                time_sum_per_number(index_prio,2) = 3.0;
            end
    end
end

% NB : number & time are two different vectors but they are linked, ex : time(1) corresponds to number(1)
% Building the time sum

% Clean number again

for i = 1 : length  % THIS IS WHERE THE NaN number error is but all NaN should have been cleaned previously ...

        % display(i) % testing the index that is NaN
        index_time = number(i);

        time_sum_per_number(index_time,1) = time_sum_per_number(index_time,1) + time(i);
end





