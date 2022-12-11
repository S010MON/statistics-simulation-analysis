%% Loading Data
data = readtable("log.txt");
length = size(data);
length = length(1);
D = zeros(max(data.number), 2);
D_creation_time = zeros(max(data.number), 1);

disp(size(D))
disp(size(D_creation_time))

%%

for i = 1:length                                    % For every element in the table
    key = data.number(i);                           % Get the event number for the element as a key
    D(key, 1) = D(key,1) + data.time(i);            % Add the time from this element to the cumulative value for the key
   
    if D(key, 2) == 0                               % Check if the priority has not already been set 
        D(key, 2) = priority(data.event(i));        % If it hasn't, then set the priority by parsing through the function
        D_creation_time(key) = data.time(i);
    end%if

end%for

D = [D(:,1), D_creation_time, D(:,1)-D_creation_time, D(:,2)];
disp(D);

%%
sum_A1 = sum(D(:,3) .* (D(:,4) == 1));               % For each priority (1, 2, 3) sum all values in the first column
sum_A2 = sum(D(:,3) .* (D(:,4) == 2));               % that have a (1 | 2 | 3) in the second column using elementwise 
sum_B = sum(D(:,3) .* (D(:,4) == 3));                % vector multiplication    


%% Analysis
nb_A1 = 0; 
nb_A2 = 0; 
nb_B = 0; 

v = D(:, 4);                                       % v is the second column of our data matrix
a1 = (v == 1);                                     % a1 is v only where v == 1
nb_A1 = v .* a1;                                   % compute vector multiplication v * a1

a2 = (v == 2);
nb_A2 = v .* a2;

b = (v == 3);
nb_B = v .* b;

D_A1 = D .* (D(:,4) == 1);
D_A1 = D_A1(:,3);
ind = find(sum(D_A1,2) == 0);
D_A1(ind,:) = [];
D_A1 = round(D_A1);

m1 = sum_A1/sum(nb_A1);                                 % Mean for patients of priority A1
m2 = sum_A2/sum(nb_A2);                                 % Mean for patients of priority A2
m3 = sum_B/sum(nb_B);                                   % Mean for patients of priority B

disp(m1);
disp(m2);
disp(m3); 

max(D_A1)
D_counts = discretize(D_A1, max(D_A1))
%hist_2  = histogram(D_counts);
%disp(hist_2);
                     

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