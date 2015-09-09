#use Time::HiRes qw(time);
#$start = time();

# author Shreyas Lakshminarayana

$doc = "cranfield0001";
$ttlcount=0;
%hashtbl = ();
$nodoc=0;
open (FW, ">tokenize.txt");

while($doc ne "cranfield1401")
{	
	open (FILE, $doc);
	@string = <FILE>;
	
	foreach $str (@string)
	{
		$str=lc($str);
		$str=~ s/\R//g;
		$str =~ s/[.\/]//g;
		$str =~ s/[,-]/ /g;
		(@array) = split(/ /,$str);
		foreach $tmp (@array)
		{
			if($tmp =~ /[A-Za-z0-9]+$/)
			{
				if($tmp ne "")
				{
					if((substr($tmp,length($tmp)-2)) eq "'s")
					{
						$tmp = substr($tmp,0,length($tmp)-2);
					}
					if(length($tmp)==1)
					{
						if($tmp eq "i" || $tmp eq "a")
						{		
							print FW $tmp; 
							print FW "\n";
							if(exists($hashtbl{$tmp}))
							{
								$temp = $hashtbl{$tmp};
								$temp += 1;
								$hashtbl{$tmp} = $temp;
							}
							else
							{
								$hashtbl{$tmp} = 1;
							}
							$ttlcount++;
						}
					}
					else
					{
						print FW $tmp; 
						print FW "\n";
						if(exists($hashtbl{$tmp}))
						{
							$temp = $hashtbl{$tmp};
							$temp += 1;
							$hashtbl{$tmp} = $temp;
						}
						else
						{
							$hashtbl{$tmp} = 1;
						}
						$ttlcount++;
					}
				}
			}
		}
	}
	close FILE;
	$doc++;
}
#$end = time();
#printf("%.5f\n", $end - $start);
close(FW);
open (FW2, ">>Pgm_Descrptn.txt");
print "\nThe total count is:$ttlcount\n\n";
print FW2  "\t\t=====================\n";
print FW2 "\t\t Program Description\n\t\t=====================\n\nThe results of the first question is as follows:\n\nThe total count is:$ttlcount\n\n";
$hashsize = keys %hashtbl;
print "The number of unique tokens in the document are: $hashsize\n\n";
print FW2 "The number of unique tokens in the document are: $hashsize\n\n";
$cnt=0;
foreach $value (values %hashtbl)
{
	if($value==1)
	{
		$cnt++;
	}
}
print "$cnt tokens occur only once\n\n"; 
print FW2 "$cnt tokens occur only once\n\n";
$x=1;
foreach $val (sort {$hashtbl{$b} <=> $hashtbl{$a}} keys %hashtbl)
{
	if($x<=30)
	{
		print "$val --> $hashtbl{$val}\n";
		print FW2 "$val --> $hashtbl{$val}\n";
		$x++;
	}
}
use integer;
$avg = $ttlcount/1400;
print "\nThe average number of words per document is: $avg\n\n";
print FW2 "\nThe average number of words per document is: $avg\n\n";
