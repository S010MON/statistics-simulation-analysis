%% Loading Data
data = readtable("log.txt")

length = size(data);
length = length(1);
D = zeros(max(data.number), 3);

for i = 1:length
    key = data.number(i);
    D(key, 1) = D(key,1) + data.time(i);
    
    % Check if the priority has not already been set
    if D(key, 2) == 0
        % If it hasn't, then set the priority by parsing through the function 
        D(key, 2) = priority(data.event(i));
    end%if
end

D

sum_A1 = sum(D(:,1) .* (D(:,2) == 1))
sum_A2 = sum(D(:,1) .* (D(:,2) == 2))
sum_B = sum(D(:,1) .* (D(:,2) == 3))



%% Parse Priority
% if priority A1   -> 1
% if priority A2   -> 2
% if priority B    -> 3
% else  return        0
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