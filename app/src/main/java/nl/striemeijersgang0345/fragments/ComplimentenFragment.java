package nl.striemeijersgang0345.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.R;

public class ComplimentenFragment extends Fragment {

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.complimentenfragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.complimentengenerator));

        TextView mCompliment = view.findViewById(R.id.complimentenText);
        mCompliment.setText(generateCompliment());

        MaterialButton mButton = view.findViewById(R.id.comp_opnieuw);
        mButton.setOnClickListener(v -> mCompliment.setText(generateCompliment()));
    }

    private static String generateCompliment() {
        final String complimentenLijst[] = {
                "Je lach is aanstekelijk.",
                "Je ziet er goed uit vandaag.",
                "Je bent een slimme kerel.",
                "Ik wed dat jij baby's aan het lachen maakt.",
                "Je hebt onberispelijke manieren.",
                "Ik houd van je stijl.",
                "Je hebt de beste lach.",
                "Je geeft mensen een goed gevoel over zichzelf",
                "Je bent de meest perfecte jou die er is.",
                "Je bent genoeg.",
                "Je bent sterk.",
                "Je bent mijn held.",
                "Wat heb je een verfrissend perspectief!",
                "Je bent een geweldige vriend.",
                "Je bent het zonnetje in huis.",
                "Je verdient een knuffel op dit moment.",
                "Bedankt voor de leuke dag!",
                "Wat heb je een prettige persoonlijkheid.",
                "Je mag trots zijn op jezelf.",
                "Je bent behulpzamer dan je denkt.",
                "Je hebt een goed gevoel voor humor.",
                "Je hebt de juiste moves!",
                "Ik weet dat ik dit aan jou kan overlaten!",
                "Op een schaal van 1 tot 10, ben jij een 11.",
                "Je bent heel makkelijk en fijn om mee te praten.",
                "Je bent dapper.",
                "Ik vind het altijd gezellig als je er bent.",
                "Je handelt naar je eigen overtuigingen.",
                "Ik ben erg van jou gecharmeerd!",
                "Je zou vaker moeten worden bedankt, dus dankjewel!",
                "Je maakt het verschil.",
                "Je bent als de zon op een regenachtige dag.",
                "Je brengt het beste in andere mensen naar boven.",
                "Je vermogen om willekeurige feiten op precies het juiste moment te herinneren, is indrukwekkend.",
                "Ik kijk enorm naar je op.",
                "Je bent een geweldige luisteraar.",
                "Hoe komt het toch dat je er altijd geweldig uitziet, zelfs in een trainingsbroek?",
                "Alles zou beter zijn als er meer mensen waren zoals jij!",
                "Ik wed dat je glitters zweet.",
                "Als iemand het kan, dan ben jij het!",
                "Jij was al cool, lang voordat hipsters cool waren.",
                "Die kleur staat je perfect.",
                "Je bent geweldig!",
                "Je weet - en zegt - altijd precies wat ik op dat moment nodig heb.",
                "Wat ruik je lekker!",
                "Ik vind het leuk om met je te praten.",
                "Je bent een echte doorzetter.",
                "Bij jou kan ik stil zijn zonder dat het ongemakkelijk voelt.",
                "Jij kan dansen alsof niemand kijkt, maar ondertussen kijkt iedereen naar wat een geweldige danser je bent!",
                "Bij jou zijn maakt alles beter.",
                "Kleuren lijken helderder als jij in de buurt bent.",
                "Je bent leuker dan een ballenbak gevuld met snoep.",
                "Je haar ziet er prachtig uit.",
                "Je bent uniek!",
                "Je bent inspirerend.",
                "Onze samenleving is beter omdat jij er bent.",
                "Je hebt de beste ideeën.",
                "Iedereen krijgt wel eens tegenslagen, maar jij weet altijd weer op te staan en door te gaan.",
                "Je bent het lichtpuntje in de duisternis.",
                "Je bent een groot voorbeeld voor anderen.",
                "Met jou omgaan is als een kleine vakantie.",
                "Jij weet altijd precies wat te zeggen.",
                "Je enthousiasme is aanstekelijk!",
                "Je bent altijd nieuwe dingen aan het leren en jezelf aan het proberen te verbeteren, dat is geweldig.",
                "Je weet me iedere keer weer te verrassen.",
                "Je hebt een goede smaak.",
                "Met jou zou ik een Zombie-Apocalyps kunnen overleven.",
                "Je bent leuker dan bubbeltjesplastic.",
                "Je bent niet bang om fouten te maken.",
                "Wie heeft jou opgevoed? Ze verdienen een medaille!",
                "Je hebt een prachtige stem.",
                "Ik ben oprecht blij dat je er bent vandaag.",
                "Je vrienden mogen zich gelukkig prijzen.",
                "Je bent een verademing!",
                "Je bent zo attent.",
                "Ik zou willen dat ik het net zo goed als jij kon doen.",
                "Ik zou helemaal niks aan je veranderen.",
                "Als jij docent was, zou ik les van je willen krijgen.",
                "Je creativiteit lijkt onbegrensd.",
                "Super dat je er bent!",
                "Je bent mijn grote voorbeeld.",
                "Als jij iets wilt, staat niets je in de weg.",
                "Je lijkt echt te weten wie je bent.",
                "Je bent de avocado op mijn toast.",
                "Er is gewoon, en dan is er jou.",
                "Je bent iemands reden om te glimlachen.",
                "Je bent zelfs beter dan een eenhoorn, omdat je echt bent.",
                "Hoe maak je toch altijd iedereen aan het lachen?",
                "Heeft iemand je ooit verteld dat je een mooie houding hebt?",
                "Je maakt me blij!",
                "Wat heb je dat creatief opgelost.",
                "Je bent echt iets bijzonders.",
                "Je bent een geschenk voor de mensen om je heen.",
                "Ik hou van jou!",
                "Ga zo door! Ik wou dat ik eruit zag zoals jij, vriend.",
                // New
                "Ik wou dat ik zoals jou kon zijn later.",
                "Ik geloof in je en ik meen het.",
                "Jij doet ertoe. Jij bent belangrijk.",
                "Ik zou helemaal niks aan je veranderen.",
                "Je bent het waard.",
                "Je zorgt ervoor dat ik een beter mens wil worden.",
                "Je inspireert me.",
                "Wat heb je een verfrissend perspectief!",
                "Met jou omgaan is altijd een feest.",
                "Wat zou ik zonder jou moeten?",
                "Hartelijk bedankt voor je bijzondere bijdrage!",
                "Ik waardeer jou.",
                "Als je iemand of iets nodig hebt ben ik er voor je.",
                "Wat ben je een inspirerend persoon. Je mag echt onwijs trots op jezelf zijn dat je op deze leeftijd al zo naar de wereld kijkt. Je bent heel wijs en neemt alles in je op.",
                "Jij bent schitterend.",
                "Talent! Je hebt een gave!",
                "Bedankt voor je innemende spontaniteit.",
                "Jij bent het beste wat mij is overkomen.",
                "Jij hebt geen idee hoe fijn het is om met mensen om te gaan die slim en scherp zijn, echt om dingen geven, en helder denken.",
                "Hey, ik mag je en ik vind je gezelschap heel prettig. We zouden meer tijd met elkaar moeten besteden.",
                "Als je bij iemand bent, ben je ook bij hem. Je geeft mensen altijd je onverdeelde aandacht, je oordeelt nooit over mensen.",
                "Ik voel me echt begrepen door jou.",
                "Dingen met jou doen is het hoogtepunt van mijn week",
                "Ik ben een beter persoon nu ik je ontmoet heb.",
                "Je hebt mijn leven veranderd.",
                "Door jou wil ik een beter persoon zijn.",
                "Jij bent de reden waarom ik constant lach (en andere mensen ook).",
                "Je bent een goed persoon.",
                "Luister, jij bent een volmaakt persoon. Je bent een volmaakt persoon en ik waardeer je.",
                "Ik ben blij je ontmoet te hebben.",
                "Ik ben blij dat ik je ken.",
                "De wereld is een betere plek met jou erin.",
                "Je bent de enige persoon die ik ontmoet heb die me continu imponeert als ik met je praat.",
                "Je bent een fantastisch persoon. Dankjewel voor het feit dat jij jij bent.",
                "Je hebt de eigenschap dat je het beste uit andere mensen haalt.",
                "Je hebt een warme persoonlijkheid.",
                "Je bent een warm persoon.",
                "Je bent heel makkelijk en fijn om mee te praten.",
                "Je hebt verborgen schatten in je.",
                "Je ziet er goed uit.",
                "Je lach maakt iedereens dag beter.",
                "Je bent een mooi, slim persoon. Mensen vinden je aardig.",
                "Ik voel dat ik je kan vertrouwen.",
                "Ik ken je als aardig en netjes, een man met integriteit en schoonheid die alle beproevingen en moeilijkheden van het leven heeft overwonnen met grootsheid en eer, en door blijft gaan met strijden door alles heen, wat een admirabele man met je moeiteloze charme!",
                "Ik kan zelfs plezier met je hebben in de hel!",
                "Je laat me veilig voelen.",
                "Jij bent mijn held.",
                "Ik zie energie, ik zie een persoonlijkheid.",
                "Je bent echt geboren om zo’n modeshowprogramma te maken op TV.",
                "Weet je, ik kan echt mezelf zijn bij jou.",
                "Wees alsjeblieft niet te hard voor jezelf. Ik zou naar je toe willen gaan, op de bank met je willen zitten, pizza eten en je favoriete film kijken als ik zou kunnen. Maar ik kan niet. Dus jij moet aardig tegen jezelf zijn omdat ik nou eenmaal verhinderd ben, oké? Beloofd?",
                "Hey jij! Ja, jij! Kom hier.",
                "Ik weet dat je het moeilijk gehad hebt. Ik zie je. Je bent een fantastisch persoon. Ondanks je moeilijkheden heb je de moed gevonden om er stevig te staan en een rolmodel te zijn. Misschien gaat het wel eens fout, maar je doet steeds weer je best en dat is te zien, vriend! Er zouden meer mensen moeten zijn die zoveel respect waardig zijn als jij. We hebben meerdere zoals jij nodig in deze wereld maar dat kunnen we niet want jij bent uniek op de best mogelijke manier. Dankjewel dat je deze persoon bent. Zet nu een lach op en hou je kin omhoog want je verdient het.",
                "Volgens mij ben jij helemaal jezelf!",
                "Je kunt echt goed luisteren.",
                "Fijn dat je er bent, mooi mens!",
                "Ik wil op jou proberen te lijken. Ik zou nu meteen mijn lichaam met jou willen ruilen.",
                "Je krijgt het voor elkaar dat wanneer je met mij praat mijn dag weer helemaal goed wordt.",
                "Ik zal mensen aanmoedigen om hun niet-lelijke kinderen naar jou te vernoemen.",
                "Ik wou dat ik er ook zo goed uitzag in mijn kleren.",
                "Ik mag jou.",
                "Ik heb je gemist man.",
                "Je straalt geluk uit.",
                "Je kunt het.",
                "Je bent awesome.",
                "Love your vibe.",
                "Jij, mijn vriend, straalt helemaal. Warm.",
                "Ik ben zo blij dat je dat met mij wilde delen, iets waar je gepassioneerd over bent. Bedankt voor dat cadeautje.",
                "Jij bent uniek. Jij bent jij. Er is geen ander persoon als jij in de wereld. Je zou goed na moeten denken over wat dat betekent. Er is geen ander persoon op de wereld als jij. Het betekent dat je je goed kunt voelen over jezelf én dat anderen zich goed kunnen voelen omdat ze jou kennen.",
                "Van binnen ben je zelfs nog mooier dan dat je dat van buiten bent.",
                "Serieus?! Het lijkt wel of je gefotoshopt bent!",
                "Je hebt een heel leuke, spontane uitstraling: omdat je contact maakt met mensen en open bent!",
                "De zon schijnt voor jou.",
                "Je bent puur en meelevend.",
                "Jij bent mijn favoriete man.",
                "Weet je wat mis is met jou? Niks!",
                "Je straalt geluk uit.",
                "Wat is je naam? Ik dacht namelijk bij mezelf: hoe heet vriendelijkheid?",
                "Je laat jezelf echt zien! Je bent anders dan anderen: uniek en authentiek!",
                "Je bent intelligent, mysterieus en mooi.",
                "Je bent de warmste van mijn medemensen!"
        };

        final String beledigingenLijst[] = {
                "Je hebt een hele mooie persoonlijkheid.",
                "Wauw, je bent slimmer dan je eruitziet.",
                "Gelukkig maar dat je zo knap bent.",
                "Je hebt echt geluk dat je geen vrouw en kinderen hebt waar je je zorgen om hoeft te maken.",
                "Het kan me niet schelen wat de rest van je vindt, ik vind je geweldig.",
                "Je bent best slim voor iemand van jouw leeftijd.",
                "Jij gaat iemand vast ooit heel gelukkig maken.",
                "In vergelijking met jou, vallen mijn problemen reuze mee.",
                "Je bent zo grappig als je dronken bent.",
                "Je hebt meer rimpels dan het scrotum van een olifant en je bent ongeveer even interessant als drogende verf.",
                "Je bent lelijker dan een emmer vol met anussen.",
                "Jij bent zo klein dat je een condoom als slaapzak gebruikt.",
                "Jij bent zo lelijk dat je moeder moest betalen om jou eruit te laten.",
                "Je bent gewoonweg niet gewenst. Laat ik proberen het zo diplomatiek mogelijk te zeggen: zelfs je baarmoeder protesteerde destijds tegen je aanwezigheid.",
                "Goh, dat is wel even wennen. Andere mensen zitten gewoon op het lichaamsdeel waar jij mee praat.",
                "Ik ben tegen discriminatie en vervolging, maar als ik jou zie kan ik daar toch wel begrip voor opbrengen.",
                "Ik vergeet nooit een gezicht, maar in jouw geval maak ik graag een uitzondering.",
                "Heb je geen reet dat je zo uit je bek stinkt?",
                "Dat je lelijk bent, daar kun je niks aan doen, maar je kunt wel thuis blijven.",
                "Jij bent zo lelijk dat uien om je huilen.",
                "Ik draag dan wel contactlenzen, maar met jou wil ik absoluut geen contact.",
                "Je hoeft niet in een boom te hangen om een eikel te zijn.",
                "Je bent niet dik hoor, je bent alleen wat klein voor je gewicht",
                "Het verschil tussen jou en een foto is dat een foto ontwikkeld is.",
                "Wat is het verschil tussen jou en een varken? Een varken wordt jou niet als hij drinkt.",
                "Als ik gemekker had willen horen, had ik wel een geit gekocht.",
                "Ik wilde je op je bek slaan, maar dat zou dierenmishandeling zijn.",
                "Leuk kapsel! Maar is het niet wat vroeg voor Halloween?",
                "Jij bent ook echt onmogelijk te onderschatten!",
                "Jij hebt een dieet waar een olifant nog niet van afvalt.",
                "Je pa had tegen de kachel moeten spuiten, dan was het nog met een sisser afgelopen.",
                "Ik wed dat jij baby's aan het huilen maakt.",
                "Als lachen het beste medicijn is, dan is jouw gezicht het beste medicijn ter wereld.",
                "Nee ik beledig je niet, ik beschrijf je alleen.",
                "Het is beter om mensen te laten denken dat je een idioot bent dan om je mond open te doen en het te bewijzen.",
                "Jouw geboortecertificaat is een verontschuldigingsbrief van de condoomfabriek.",
                "Jij bent het bewijs dat zelfs God wel eens fouten maakt.",
                "Jij bent zo nep dat Barbie jaloers is!",
                "Ik ben jaloers op mensen die jou niet kennen!",
                "Jij bent zo lelijk dat je moeder een boete kreeg voor het weggooien van afval.",
                "Als ik zelfmoord zou willen plegen, zou ik bovenop je ego klimmen en naar je IQ duiken.",
                "Jij bent vast geboren op een snelweg want daar gebeuren de meeste ongelukken.",
                "Schoonheid is niet alles. In jouw geval is het niets.",
                "Ik weet niet wat jou zo dom maakt, maar het werkt echt heel goed.",
                "Jouw een idioot noemen is een belediging voor alle idioten.",
                "Sommige ouders hebben hun baby's laten vallen, maar volgens mij hebben ze jou dagelijks tegen een muur gegooid.",
                "Hou alsjeblieft je mond wanneer je tegen me praat.",
                "Ze zeggen dat tegenpolen elkaar aantrekken. Ik hoop dat je iemand tegenkomt die intelligent, knap en beschaafd is.",
                "De laatste keer dat ik iets zag zoals jij, spoelde ik het door.",
                "Als lelijk zijn een misdaad was, zou jij levenslang krijgen.",
                "Je brein is op vakantie maar je mond maakt overuren.",
                "Verras me eens en zeg wat intelligents.",
                "Je bent niet zo dom als mensen zeggen. Je bent veel, veel erger.",
                "Ik heb wel eens vaker mensen zoals jij gezien, maar toen moest ik toegangsgeld betalen.",
                "Ben je laatst wezen winkelen? Ik heb gehoord dat ze levens verkopen en jij kan er nog wel een gebruiken.",
                "Jij bent zoals maandagmorgens, niemand houdt ervan.",
                "Natuurlijk praat ik als een idioot; hoe zou je me anders moeten begrijpen?",
                "Ik heb de hele dag aan je gedacht... Ik was in de dierentuin.",
                "Jij bent zo dik dat je schaduw kan verkopen.",
                "Heb je geen vergunning nodig om zo lelijk te zijn?",
                "Elke keer als ik bij je ben, krijg ik een sterk verlangen om alleen te zijn.",
                "Jij bent zo dom dat je nog aangereden wordt door een geparkeerde auto.",
                "Blijf praten, ooit zeg je vast wat intelligents!",
                "Jij bent zo dik dat je voetafdrukken achterlaat in het beton.",
                "Ben je altijd zo dom of is dit een speciale aangelegenheid?",
                "Ze zeggen dat schoonheid van binnen zit. Je kan maar beter hopen dat dat waar is.",
                "Jij bent zo lelijk dat je portret zichzelf heeft opgehangen.",
                "Soms wil ik iets wat alleen jij me kan geven: je afwezigheid.",
                "Je minderwaardigheidscomplex is compleet gerechtvaardigd."
        };

        // Bepaal debielentax
        double debielentax = 1;
        if(MainActivity.USER.equals(MainActivity.WILLEM))
            debielentax = 1.2;
        if(MainActivity.USER.equals(MainActivity.THIJS))
            debielentax = 0.75;

        // Bepaal of er een compliment of belediging getoond moet worden
        int n = complimentenLijst.length + beledigingenLijst.length;
        int prob =(int) Math.floor(Math.random() * n * debielentax);

        // Genereer een nieuw getal voor de juiste lijst
        String str;
        if(prob <= complimentenLijst.length)
            str = complimentenLijst[(int) Math.floor(Math.random() * complimentenLijst.length)];
        else
            str = beledigingenLijst[(int) Math.floor(Math.random() * beledigingenLijst.length)];

        // Bepaal of " hoor!" toegevoegd moet worden
        double sarcasmetax = 0.2;
        if(MainActivity.USER.equals(MainActivity.THOMAS))
            sarcasmetax = 0.3;
        if(MainActivity.USER.equals(MainActivity.THIJS))
            sarcasmetax = 0.1;
        if(Math.random() <= sarcasmetax)
            str = str.substring(0, str.length() - 1) + " hoor!";
        else if(Math.random() <= 0.1) {
            // Als het niet sarcastisch is, haal dan in 10% van de gevallen de naam erbij
            if(Math.random() <= 0.5) {
                char c[] = str.toCharArray();
                c[0] = Character.toLowerCase(c[0]);
                str = new String(c);
                str = MainActivity.USER + ", " + str;
            }
            else {
                char c[] = str.toCharArray();
                char last = Character.toLowerCase(c[c.length - 1]);
                str = str.substring(0, str.length() - 1) + ", " + MainActivity.USER + last;
            }
        }

        return(str);
    }
}
