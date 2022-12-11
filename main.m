%% Loading Data
data = readtable("log.txt");
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
D(:,3) = D(:,2) - D(:,1);
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
xlabel("Time (mins?)")
ylabel("No. of Patients")
hold off;

%% Chi Squared Test
clear length
N = length(D_A1)

s = log( (1/N) * sum(D_A1(:,3)) ) - ((1/N) * sum(log(D_A1(:,3))) )

k = (3 - s + sqrt((s-3)^2 + 24*s))/(12*s)

theta = sum(D_A1(:,3))/(k*N)

alpha = k
beta = 1/theta

function_dat1 = @(x) (( x^(alpha-1)*exp(-beta*x)*beta^alpha ) / gamma(alpha - 1))

h = histfit(D_A1(:,3),100,'gamma')
gamfit(D_A1(:,3))

Bins_size = zeros(1,100);
ax = gca;
alpha_chi = 0.05;
chi = 0;
null_hypo = function_dat1;
for i=1 : 100
    Bins_size(i) = ax.Children(1).Values(i);
    chi = chi + ( (Bins_size(i)-null_hypo(1)).^2 ) / null_hypo(1);
end
chi

chi2inv(0.95,100-1)
%% Chi Square Test
pdf = @(x) gampdf(x,alpha,beta);

chi_square = 0;

% I define my boundaries, my 'bins'.
boundaries = []; 
cump = 0:0.01:1; 
for j = 1 :  numel(cump)
    boundaries = [boundaries, gaminv(cump(j),alpha,beta)]; 
end

% I need to count the number of occurences in each bin :
N = histcounts(D_A1, boundaries); 
n = sum(N); 


for count = 1 : length(boundaries)-1

 
        p = integral(pdf,boundaries(count),boundaries(count+1));
        
  
        chi_square = chi_square + (((N(count)-(n*p))^2)/(n*p));
end

display(chi_square/1000);
chi2inv(0.95,100-1) %123.2252
%% 95% confidence interval   
clear length
x = D_A1(:,3);                              % Create Data
SEM = std(x)/sqrt(length(x));               % Standard Error
ts = tinv([0.025  0.975],length(x)-1);      % T-Score
CI = mean(x) + ts*SEM;                      % Confidence Intervals                      

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