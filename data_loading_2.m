%% Loading Data
data = readtable("log.txt")
length = size(data);
length = length(1);
D = zeros(max(data.number), 3);

for i = 1:length                                    % For every element in the table
    key = data.number(i);                           % Get the event number for the element as a key
    D(key, 1) = D(key,1) + data.time(i);            % Add the time from this element to the cumulative value for the key
   
    if D(key, 2) == 0                               % Check if the priority has not already been set 
        D(key, 2) = priority(data.event(i));        % If it hasn't, then set the priority by parsing through the function
    end%if
end%for

disp(D)

sum_A1 = sum(D(:,1) .* (D(:,2) == 1))               % For each priority (1, 2, 3) sum all values in the first column
sum_A2 = sum(D(:,1) .* (D(:,2) == 2))               % that have a (1 | 2 | 3) in the second column using elementwise 
sum_B = sum(D(:,1) .* (D(:,2) == 3))                % vector multiplication    

function output = priority(str)

    output = 0;

    if strcmp(str, 'New patient priority A1')
        output = 1;
    elseif strcmp(str, 'New patient priority A2')
        output = 2;
    elseif strcmp(str, 'New patient priority B')
        output = 3;
    end
end%function