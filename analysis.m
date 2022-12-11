%% Loading Data
data = readtable("log.txt");
length = size(data);
length = length(1);
D = zeros(max(data.number), 2);

for i = 1:length                                    % For every element in the table
    key = data.number(i);                           % Get the event number for the element as a key
    D(key, 1) = D(key,1) + data.time(i);            % Add the time from this element to the cumulative value for the key
   
    if D(key, 2) == 0                               % Check if the priority has not already been set 
        D(key, 2) = priority(data.event(i));        % If it hasn't, then set the priority by parsing through the function
    end%if
end%for

% disp(D)

sum_A1 = sum(D(:,1) .* (D(:,2) == 1));               % For each priority (1, 2, 3) sum all values in the first column
sum_A2 = sum(D(:,1) .* (D(:,2) == 2));               % that have a (1 | 2 | 3) in the second column using elementwise 
sum_B = sum(D(:,1) .* (D(:,2) == 3));                % vector multiplication    


%% Analysis
nb_A1 = 0; 
nb_A2 = 0; 
nb_B = 0; 

v = D(:, 2);                                       % v is the second column of our data matrix
a1 = (v == 1);                                     % a1 is v only where v == 1
nb_A1 = v .* a1;                                   % compute vector multiplication v * a1

a2 = (v == 2);
nb_A2 = v .* a2;

b = (v == 3);
nb_B = v .* b;

D_A1 = D .* (D(:,2) == 1);
D_A1 = D_A1(:,1);
ind = find(sum(D_A1,2)==0);
D_A1(ind,:) = [];
D_A1 = round(D_A1);

m1 = sum_A1/nb_A1;                                 % Mean for patients of priority A1
m2 = sum_A2/nb_A2;                                 % Mean for patients of priority A2
m3 = sum_B/nb_B;                                   % Mean for patients of priority B

disp(m1)
disp(m2)
disp(m3)



nb_bins = 10;                                   % Number of bins
hist = histogram(D_A1, 'Orientation', 'horizontal');                              % Plotting ~ ressembles normal distribution
disp(hist.BinCounts)

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