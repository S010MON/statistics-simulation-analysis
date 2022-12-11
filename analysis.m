%% Loading Data
data = readtable("logs/log.txt");
length = size(data);
length = length(1);

% Create a data array where columns represent:
% | first_time | last_time | delta_time | priority
% with n rows, where n = number of distinct events
D = zeros(max(data.number), 4);
disp(size(D))

for i = 1:length                                    % For every element in the table
    key = data.number(i);                           % Get the event number for the element as a key
   
    if D(key, 4) == 0                               % Check if the priority has not already been set (i.e. unvisited)
        D(key, 4) = priority(data.event(i));        % If it hasn't, then set the priority by parsing through the function
        D(key, 1) = data.time(i);                   % Add the time from this element to the start_time for the key
    else
        D(key, 2) = data.time(i);                   % Add the time from this element to the cumulative value for the key
    end%if

end%for


% Remove all rows where col 2 is zero
D( all(~D(:,2),2), : ) = [];


% Calculate the difference between col 1 and col 2
D(:,3) = (D(:,2) - D(:,1));
disp(D);

% Select all A1 priority data only
D_A1 = D .* (D(:,4) == 1);
D_A1( all(~D_A1(:,2),2), : ) = [];
disp(D_A1);

% Create a histogram of A1 priority processing times
figure(1)
hist = histogram(D_A1, 100);
hold on;
title("Processing Times for A1 Priority Patients");
xlabel("Time (mins)")
ylabel("No. of Patients")
hold off;

%% Chi Squared Test


%% K-S Test
                     

%% Function Definitions
function output = priority(str)

    output = 0;

    if strcmp(str, 'New patient priority A1')
        output = 1;
    elseif strcmp(str, 'New patient priority A2')
        output = 2;
    elseif strcmp(str, 'New patient priority B')
        output = 3;
    end%if
end%function