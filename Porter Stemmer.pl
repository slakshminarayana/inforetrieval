# author Shreyas Lakshminarayana

$cntmeasure = 0;
$strcnt = 0;
open (FILE, "tokenize.txt");
@array = <FILE>;

%hashtbl = ();
print "\n";

sub findmeasure
{
	$i = 0;
	$j = 0;
	$a = 0;
	$k = 0;
	$cntV=0;
	$cntC=0;
	@meas=();
	@uncompresd=();
	while($i < $strcnt)
	{
		if($i == 0)
		{
			if($strarray[$i] =~ /[aeiou]/)
			{ 
				$uncompresd[$j] = "V";
				$cntV++;
				$j+=1;
				$i+=1;
			}
			else
			{	 
				$uncompresd[$j] = "C";
				$cntC++;
				$j+=1;
				$i+=1;
			}
		}
		else
		{
			if($strarray[$i] =~ /[y]/)
			{
				if($uncompresd[$j-1] eq "C")
				{
					$uncompresd[$j] = "V";
					$cntV++;
					$j++;
					$i+=1;	
				}
				else
				{
					$uncompresd[$j] = "C";
					$cntC++;
					$j++;
					$i+=1;
				}
			}
			elsif($strarray[$i] =~ /[aeiou]/)
			{ 
				$uncompresd[$j] = "V";
				$cntV++;
				$j+=1;
				$i+=1;
			}
			else
			{ 
				$uncompresd[$j] = "C";
				$cntC++;
				$j+=1;
				$i+=1;
			}
		}
	}
	
	while($a < $#uncompresd+1)
	{
		if($uncompresd[$a] eq "V")
		{
			$meas[$k] = "V";
			$k++;
			$a++;
			while($uncompresd[$a] =~ /[V]/ && $a < $#uncompresd+1)
			{
				$a++;
			}
		}
		else
		{
			$meas[$k] = "C";
			$k++;
			$a++;
			while($uncompresd[$a] !~ /[V]/ && $a < $#uncompresd+1)
			{
				$a++;
			}
		}
	}
}


sub countmeasure
{
	$r=0;
	$cntmeasure = 0;
	while($r < $#meas)
	{ 
		if(($meas[$r] eq "V") && ($meas[$r+1] eq "C"))
		{
			$cntmeasure++;	
			$r+=2;
		}
		else
		{
			$r+=1;
		}
	}
}

sub step1
{
	if((substr(reverse($string),0,length("sses"))) eq (reverse("sses")))
	{
		$string = substr($string,0,length($string)-length("sses")+2);
	}
	elsif((substr(reverse($string),0,length("ies"))) eq (reverse("ies")))
	{
		$string = substr($string,0,length($string)-length("ies")+1);
	}
	elsif((substr(reverse($string),0,length("ss"))) eq (reverse("ss")))
	{
	}
	elsif((substr(reverse($string),0,length("s"))) eq (reverse("s")))
	{
		$string = substr($string,0,length($string)-length("s"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("eed"))) eq (reverse("eed")))
	{
		$string = substr($string,0,length($string)-length("eed")+2);
	}
	elsif($cntV > 1 && (substr(reverse($string),0,length("ed"))) eq (reverse("ed")))
	{
		$string = substr($string,0,length($string)-length("ed"));
		if(substr(reverse($string),0,length("at")) eq (reverse("at")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		elsif(substr(reverse($string),0,length("bl")) eq (reverse("bl")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		elsif(substr(reverse($string),0,length("iz")) eq (reverse("iz")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		else
		{
		}
	}
	elsif($cntV > 1 && (substr(reverse($string),0,length("ing"))) eq (reverse("ing")))
	{
		$string = substr($string,0,length($string)-length("ing"));
		if(substr(reverse($string),0,length("at")) eq (reverse("at")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		elsif(substr(reverse($string),0,length("bl")) eq (reverse("bl")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		elsif(substr(reverse($string),0,length("iz")) eq (reverse("iz")))
		{
			$string = "$string e";
			$string =~ s/ //g;
		}
		else
		{
		}
	}
	elsif($cntV > 1 && (substr(reverse($string),0,length("y"))) eq (reverse("y")))
	{
		$string = substr($string,0,length($string)-length("y"));
		$string = "$string i";
		$string =~ s/ //g;
	}
	else
	{
	}
}

sub step2
{
	if($cntmeasure > 0 && (substr(reverse($string),0,length("ational"))) eq (reverse("ational")))
	{
		$string = substr($string,0,length($string)-length("ational")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("tional"))) eq (reverse("tional")))
	{
		$string = substr($string,0,length($string)-length("tional")+4);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("enci"))) eq (reverse("enci")))
	{
		$string = substr($string,0,length($string)-length("enci")+3);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("anci"))) eq (reverse("anci")))
	{
		$string = substr($string,0,length($string)-length("anci")+3);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("izer"))) eq (reverse("izer")))
	{
		$string = substr($string,0,length($string)-length("izer")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("abli"))) eq (reverse("abli")))
	{
		$string = substr($string,0,length($string)-length("abli")+3);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("alli"))) eq (reverse("alli")))
	{
		$string = substr($string,0,length($string)-length("alli")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("entli"))) eq (reverse("entli")))
	{
		$string = substr($string,0,length($string)-length("entli")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("eli"))) eq (reverse("eli")))
	{
		$string = substr($string,0,length($string)-length("eli")+1);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ousli"))) eq (reverse("ousli")))
	{
		$string = substr($string,0,length($string)-length("ousli")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ization"))) eq (reverse("ization")))
	{
		$string = substr($string,0,length($string)-length("ization")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ation"))) eq (reverse("ation")))
	{
		$string = substr($string,0,length($string)-length("ation")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ator"))) eq (reverse("ator")))
	{
		$string = substr($string,0,length($string)-length("ator")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("alism"))) eq (reverse("alism")))
	{
		$string = substr($string,0,length($string)-length("alism")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("iveness"))) eq (reverse("iveness")))
	{
		$string = substr($string,0,length($string)-length("iveness")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("fulness"))) eq (reverse("fulness")))
	{
		$string = substr($string,0,length($string)-length("fulness")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ousness"))) eq (reverse("ousness")))
	{
		$string = substr($string,0,length($string)-length("ousness")+3);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("aliti"))) eq (reverse("aliti")))
	{
		$string = substr($string,0,length($string)-length("aliti")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("iviti"))) eq (reverse("iviti")))
	{
		$string = substr($string,0,length($string)-length("iviti")+2);
		$string = "$string e";
		$string =~ s/ //g;
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("biliti"))) eq (reverse("biliti")))
	{
		$string = substr($string,0,length($string)-length("biliti")+1);
		$string = "$string le";
		$string =~ s/ //g;
	}
	else
	{
	}	
}

sub step3
{
	if($cntmeasure > 0 && (substr(reverse($string),0,length("icate"))) eq (reverse("icate")))
	{
		$string = substr($string,0,length($string)-length("icate")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ative"))) eq (reverse("ative")))
	{
		$string = substr($string,0,length($string)-length("ative"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("alize"))) eq (reverse("alize")))
	{
		$string = substr($string,0,length($string)-length("alize")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("iciti"))) eq (reverse("iciti")))
	{
		$string = substr($string,0,length($string)-length("iciti")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ical"))) eq (reverse("ical")))
	{
		$string = substr($string,0,length($string)-length("ical")+2);
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ful"))) eq (reverse("ful")))
	{
		$string = substr($string,0,length($string)-length("ful"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ness"))) eq (reverse("ness")))
	{
		$string = substr($string,0,length($string)-length("ness"));
	}
	else
	{
	}
}

sub step4
{
	if($cntmeasure > 0 && (substr(reverse($string),0,length("al"))) eq (reverse("al")))
	{
		$string = substr($string,0,length($string)-length("al"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ance"))) eq (reverse("ance")))
	{
		$string = substr($string,0,length($string)-length("ance"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ence"))) eq (reverse("ence")))
	{
		$string = substr($string,0,length($string)-length("ence"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("er"))) eq (reverse("er")))
	{
		$string = substr($string,0,length($string)-length("er"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ic"))) eq (reverse("ic")))
	{
		$string = substr($string,0,length($string)-length("ic"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("able"))) eq (reverse("able")))
	{
		$string = substr($string,0,length($string)-length("able"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ible"))) eq (reverse("ible")))
	{
		$string = substr($string,0,length($string)-length("ible"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ant"))) eq (reverse("ant")))
	{
		$string = substr($string,0,length($string)-length("ant"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ement"))) eq (reverse("ement")))
	{
		$string = substr($string,0,length($string)-length("ement"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ment"))) eq (reverse("ment")))
	{
		$string = substr($string,0,length($string)-length("ment"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ent"))) eq (reverse("ent")))
	{
		$string = substr($string,0,length($string)-length("ent"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ou"))) eq (reverse("ou")))
	{
		$string = substr($string,0,length($string)-length("ou"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ism"))) eq (reverse("ism")))
	{
		$string = substr($string,0,length($string)-length("ism"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ate"))) eq (reverse("ate")))
	{
		$string = substr($string,0,length($string)-length("ate"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("iti"))) eq (reverse("iti")))
	{
		$string = substr($string,0,length($string)-length("iti"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ous"))) eq (reverse("ous")))
	{
		$string = substr($string,0,length($string)-length("ous"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ive"))) eq (reverse("ive")))
	{
		$string = substr($string,0,length($string)-length("ive"));
	}
	elsif($cntmeasure > 0 && (substr(reverse($string),0,length("ize"))) eq (reverse("ize")))
	{
		$string = substr($string,0,length($string)-length("ize"));
	}
	else
	{
	}
}

sub step5
{
	if($cntmeasure > 1 && (substr(reverse($string),0,length("e"))) eq (reverse("e")))
	{
		$string = substr($string,0,length($string)-1);
	}
	elsif($cntmeasure > 1 && $uncompresd[length($string)-1] eq "C" && $uncompresd[length($string)-2] eq "C" && (substr(reverse($string),0,length("l"))) eq (reverse("l")))
	{
		$string = substr($string,0,length($string)-length("l"));
	}
}

$ttlcnt = 0;

foreach $itm (@array)
{
	$string = $itm;
	chomp($string);
	@strarray = split(//,$string);
	$strcnt = $#strarray+1;
	findmeasure;
	countmeasure;
	step1;
	step2;
	step3;
	step4;
	step5;
	$ttlcnt+=1;
	if(exists($hashtbl{$string}))
	{
		$temp = $hashtbl{$string};
		$temp += 1;
		$hashtbl{$string} = $temp;
	}
	else
	{
		$hashtbl{$string} = 1;
	}
}
$hashsize = keys %hashtbl;
print "The number of unique tokens in the document are: $hashsize\n\n";
open (FE, ">>Pgm_Descrptn.txt");
print FE "The results of the second questions are as follows:\n\n";
print FE "The number of unique tokens in the document are: $hashsize\n\n";
$cnt=0;
foreach $value (values %hashtbl)
{
	if($value==1)
	{
		$cnt++;
	}
}
print "$cnt tokens occur only once\n\n"; 
print FE "$cnt tokens occur only once\n\n"; 
$x=1;
foreach $val (sort {$hashtbl{$b} <=> $hashtbl{$a}} keys %hashtbl)
{
	if($x<=30)
	{
		print "$val --> $hashtbl{$val}\n";
		print FE "$val --> $hashtbl{$val}\n";
		$x++;
	}
}
use integer;
$avg = $ttlcnt/1400;
print "\nThe average number of word document is $avg\n\n";
print FE "\nThe average number of word document is $avg\n\n";
close(FE);
