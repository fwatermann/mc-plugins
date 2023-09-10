package de.legitfinn.NickApi.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;

public class main extends JavaPlugin implements Listener {

	public static HashMap<UUID, String> uuid_name = new HashMap<UUID, String>();
	public static HashMap<UUID, String> uuid_nickname = new HashMap<UUID, String>();
	public static HashMap<String, UUID> nickname_uuid = new HashMap<String, UUID>();
	public static HashMap<String, String> nickname_name = new HashMap<String, String>();
	public static ArrayList<String> nicknames = new ArrayList<String>();
	
	
	public void onEnable() {
		PlayerData.mysql = new MySQL(SignAPI.getIp(), "PlayerData1", "PlayerData1", "Kk1QxhxyGsOdw7xQ", this);
		PlayerData.mysql.update("CREATE TABLE IF NOT EXISTS PlayerData(UUID varchar(128), NAME varchar(128))");
		PlayerData.mysql.update("CREATE TABLE IF NOT EXISTS TEXTURE(UUID varchar(128), VALUE varchar(1024), SIGNATUR varchar(1024))");
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginCommand("showNick").setExecutor(new COMMAND_showNick());
		nicknames = PlayerData.getAllNames();
	}
	
	public void onDisable() {
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent e) {
		CraftPlayer cp = (CraftPlayer)e.getPlayer();
		GameProfile profile = cp.getProfile();
		Collection<Property> col = profile.getProperties().get("textures");	
		for(Property all : col) {
			String value = all.getValue();
			String signatur = all.getSignature();
			PlayerData.setSkinTexture(cp.getUniqueId(), value, signatur);
			return;
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(NickModul.isNicked(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage("§7[§cNICK§7] §cDu wirst nun als §e" + uuid_nickname.get(e.getPlayer().getUniqueId()) + "§c angezeit.");
			e.getPlayer().sendMessage("");
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(NickModul.isNicked(e.getPlayer().getUniqueId())) {
			String nickname = uuid_nickname.get(e.getPlayer().getUniqueId());
			uuid_name.remove(e.getPlayer().getUniqueId());
			uuid_nickname.remove(e.getPlayer().getUniqueId());
			nickname_uuid.remove(nickname);
			nickname_name.remove(nickname);
		}
	}	
	
	public static String getRandomName() {
		if(nicknames.size() <= 10) {
			String NameList = "";
			if(NameList.isEmpty()) {
				NameList = "12Erianni15,139Vilu,19Pluto19,1lamaa,20Luis05,28min,2wag1geUnt3rh0se,5Ilja5,7lego7,9Minecraftplayer,__PUPPII__,__UnLeqitBoy,_Darkin,_DieserJunge_,_Flixii,_SpectraHD_,_SweetUnicorn_,Abgehobene,Abukrenar,Acie_HD,Adi1511,Alchimist_,alex456601,Alex_tja,Alexloo2004,Alphapenner,Ammannjj04,AngiHH,AnimeStudio,Arm3sputput,BadxGuurlx3,BananaJoe88,Banani44,bannanypays,Beexi,Bendgames_HD,bentoisgehtmitte,biglion1,BisaFanLP01,BlackdragonBoy,Bloxxboss,BlueYetiYT,Blumare,BugEnteDanny,Bulle2014,Bundesheer,BunnycraftHD,byNisi,Caandy_,Calany,CaptainSilas,Chasa,ChrisMagJenny,ckoky,clashiiiiiiii,Colin_55,CookieKeks214,coolerBunnyFna,Coyotitofirst,CraftgamerHD123,Cryuu,CubixRage,CuteNoah,CuzIm_Trix,CuzImKoala,Cytrem,D4rkForceG,Daiichii,dannebannan,darksplitter_,DasVergessene,Daviii,DB_Regio_Bayern,DennisMussWeinen,Der_KEKSMAPFER,DerBedwarsLehrer,DerHDSchokoGamer,DieLeertaste,diePaedagogin,Digger1981,DisMainNamePuten,DoItLikeObama,DoomNikkei,Dstroyer_Exo,EinfachHenni,EinfachNur_Kevin,EinfachSchwarz,ElPeker,endercraft2907,Entroo,Erlebnis,EventixHD,ExtraPrnxFvllYT,F4c3_p4lm,FabsyHD,faridbang2005,FasterThanAudi,FeuerwehrDaniel,Foxy3500,FrediFuchs,FreeEpicBoy,FresherSteve,G007M,galeksss,gamernick07,gaming200,gedankenspiel,Gichii,Glubsch1,Grimlock_CH,HabKeinWLAN,hackedVyleex,HardcoreGamer77,HarmlosesVenex,HateMitte,Hausi3454,Heldiggrisen,HellDevil03,Herodaniel12,IBW_3001,IceMageYTREAL,IchHabDirGesagt,IchMagdieMitteLP,IchMagFerdii,IchSaveDichNicht,InternetLarry,Its_a_Isii,Its_a_Tabii,iTz_Craz,ItzLinexPvP,itzSw1dy,Jahwe,Jamasteve,JasminMagJannes,Jerrwow,joel_46,jonasgg,Julia294,julian12358,JuliusMats2006,juls2002,Juno2906,Just4Umpii,Jxssinski_PvP,KalEl_Magno,KarotteHD,katerina988778,KazeShouho,KlebrigesTesa,KnuddelBaerchie,Knusperkeks04,l3sch,LabyModBesitzer,LadyMimi,Lanaxforexerxx,lebhaftHD,leMineralWasser,LennoxPlayzZ,LeqitFabio,leqitPandaa,Lexi00,Life4Gaming,lilGuz,LiquidMovement,lKevinl,lnaktiv,Lobbyzocker,LopicCraft,LP_Luna,Lukas__YT,LukiiHD,LuRoB,MakingTV,Marlon_2002,Marvin999,Matteo325,McGepard,MEGA_CJ,Megastar3306,MeinMagenKnurrt,Miezunder,Miilena,Milano_Zockt,minecraftschacht,minermalik,MiniOG,MklRbr,Mlg_Panda_Bruder,moirap,Mombe_HD,MrCookie003,MrPolyp,MrsMoonShiine,Multishowdog,N00b_,n02_,NenaHD,NorwinLP,Ntee_,Nuqoo,Nutzername950,nvruto,Offenz,ordyx,padebos,PalmnAusPlastik,palqix,pantoffelman,PatausHos,PaySafeJojo,pepe2412,phcrafterlp,Philipp87,Philipstarwars,Pinguinek,Pix3lHero,plexiglasjunge,Proxiii,PusherMarcel,PXL_ReKTzz,PXLCraft_,qaal,qBlvck,qutemanda,qxriz,R3alMicro,R3dCircle,raZerMKE,Redslam070,RedSlimeEngineer,RedTool_,ReoxDesiignz,RichardStrieder,rocky_rabbit_1,Romuni,RotKick,S_A_N_D_R_O,Saahne,sakuLS,SakuraLy,Samlp,Saxony,schbastighg,Schrian,SchwarzWeis,Scylley,Sebo_HD,Seltixh,semteax,Sick2009,Siiimxxn,sk29cs,Snichlater,Snitchi,SourceException,SprichMichAn,StammPrdicted,Starsilberhd,SteveAufDroge,stormer100,SuchiiiOP_,Sucukk,SuperGirl112,Sweet___Cookie,Sweet_Girl_,Talon76,TamiTalea,TamTam2004,TechMiner5000,Terminator20000,ThatsMyBae,TheBrownyyy,TheCrash3,TheDarkNightWolf,TheFoxcraft,TheMonstaHD,Tholley,Timo9200,ToggelLP,tomatencraft,Traint,Tremnac,Triumpf,Trxst,Tschekem,TSM_NSJL,TuhleMitte,UmarmeMich,UnlegitSunny,UnleqiitLeon,UselessBen,V3nusG4m3r,Vacien,Venquaz,Vincentos,Voartex_Plays,Vokabelheft,Vompex,vxrgiftet,warmat74,WilhelmXXV,WillowDyke,Winkey,xApoRed,xDinozZO,xEnjoy30ms,XF1ght3rm4nn,xkillernation,xRobZone,Xstrick,xTelevision,xXJul_ienXx,xXKeihneAhnungXx,xXSchoko_HDXx,yotu,zCuzImKivi,zFr0z3n,Zock3rJul3_01,_Waschmaschine_2,a44b,adryan_br,aku313ankka,Antobat,auroraqueencraft,Bastien360,CPC_Falcover,creeperking570,Denox__,diamondleigon33,DrPepster,EnderMiner33,Fera_Craft,GamesPower,Gaming_Wolf_269,geek_stuff,JCV03,Jerky_360,JewKillerCaillou,Jupsteri,kristianbli,luki2102,MariaSoledad_AYK,Maurice_24,MentallyImpunged,MouthOnMyBalls,Nerve2023,Noobworkking,Norahtje,Platinum5,PlaySmart,poisonstone9040,PopThatKat,RainbowDood,ruarster999,SandWidge,SkyDiva101,Slimey_Pvper,SpencerGamer,Sun_Bunni,SuperSophie2001,ThePhoenix215,TVTL05,Volkanrowie,XxBennettTRMxX,Yeezy121,007PP,50er,_AntiKnxck_,_DDlp,_HerthaGHG_,_ImNico_,AliHD1_Gaming,Ambargo,ASSIat,AsylantMajin,BatmanSeinVater,BeisserHD,Beno1504,BenyyyyGHG,Berkan_HD,BertyyyGHG,BionicShockWave,BossOfTNT,boysforever,ButterGlaze,ByLino,CountBanana,CraftingGamer_lp,creativedoctor,Cry0nic,DarkShadow09,DarkYelloX,DerEchteMuchael,EismannStyle,EismitStil,EmpireAnas,EnoughOfYou,FlohCrafter,flowerore,FrshLikeJan,FunnyPotato,GeneralLumpi,geRxped_,Goldencraft11,gomit_wizard,GX200Dean,HackedByVentox_,HannsBossGaming,hofmi,HotDogLP007,I_am_Koray,Ibisevic,Ice_CreamPvP,Ice_CreamTv,IchGehDown,IchQuitDochNur,ImArizxna,ImMitteClient,itsme1,itz_Entenplays,JaniJani1500,johbr,joku3,KeyFoxGHG,KnockbackDino,KonekoMiaow,le_Pingu,LegendaryErva,leSyreax,Lfrsh,LKWThomas,Lous,LucaTex,Luizifer,lutexx,MalkamsBFF,Malorex,Manjo04,MattiNummer1,Megablox03,MrJiz,MrJoz,MrsScaffoldwalk,MxlvxnAC,NixhtFxlix,Obried03,ohmeingott,Ollifred,Op_Finn,Philabs,PigkillFordder,PinkSnow,Pixl9,Plassma,quteMoritz,rattenschatten,RevolutionLN17,RiBroHD,Rizex45,schw3igi,schwaeche,Schwarzgeld,Senshi,ShadowShield,SiicerClicker,SkalesXX,SkorpTV,Skrillex_KC,SpyrowNick,STERNTENDO,SuperChocolat5,SZSchnitte,TARNiko,The_Monster_Kill,TheBrothers123,TheIceFireHD,TheViiZionJr,TiischleX,Tillmit2L,Tlmmey,Toreq,Triisss,Waffel909,Xalon,xKing03x,xLegitSoupPvP,xXReaTzXx,xXSkelltHDXx,yokolafrite,YoloPlayer2002,Ziireax,zRelvxn,__glow,_Qrix,_TomGHG_,_Vladi_,_xXPixpvpXx_,AiRNiFeR,blackmonkeay,blockFighGHG7,Bubble3D,Busausweis,By_Snow,byBader,ByMarviin,Clesox,Cqlm_,daavin,Danigespielt,DePaulchen,DerHodenSack,DomAb,dxqbee,EnjoyMyKnock,Erstklassig,Fabi7777,FenBabianLP,FeuerwehrMateo,FireBlazeXd,Fishingmaster123,flobo_poe2002,FloteXXo,forcacraft,Fre3zeaxBw,Frxdie,girofar1,GuliHD,Haiiise,Harksi,heikser,HerrBen,IchBinHuhn,IchWillTorten,ImAlexD,ImLeques,ImRazeax,Ir0nFalke,Its_MaxTheMax_,Its_SevenEleven,iTzEasyWin,ItzPhoenixHd,Juian14,Kiddyyyy,King_Jooooonge1,KitoxGHG,Kuitoo,LeifTV,leovinci03,LesneyHD,LionexHD,Liqht_Low,LiquitPanda,Loukas1308,LP_aza,LPGregHD,lukas06243,marlonbxxl,Matteoexpert,MBoy_HD,moritz_craft,MrJBPlay,MrMacMulloh,Mynze,NapBenny,Neuntrox,NigiHD,NoNameSouper,ObsidianArmor,P1gu1_Koobbee,PezWuig,PolizistLuca,QetOnMyLvL,RealMath,Reaper2003,Reaxyy,redfireone,RelixChroma,Roofen,Rutituti,Shantiwhatelse,Shima_04,Simeonsport,SkillerChillerHD,SkillsTime,SkyAsyt,SoulDestroyer_,Spikegrieft,Sturmwall_11,Swat_Vibes12,TaqticalFlexy,Tashaa77,the_killer_largo,thesabonis007,ThunderJonas,Tim_MC_,tindon,TinoBeckiGG,tobiasistgeil,Tomskilll,Venrec,VeraFTW,wienerschnitzel4,Wolvon,xHitsLike_Real,xImCombo_sheesh,xImLeqitValique,xItachyyy,xSquadable,xXPyrox_Xx,xXTobiPvPXx,xXVantoXx,xXYusuf2005PvPXx,zAgroo,zArabRanqe_,ZarosGHG,Zejdin,zImHengez,2000babs,2fett4flyhack,_BlueBall_,_DIGGICAM_,_lordSreax_,_sweetSkill,AgroPlayerLP,Alex_2K4,Bene_1,BetaJoshi,birdyy,BlockPlayz,Brause_Jonas,ByExpertGHG,byrealcraft,Cola__HD,Convertex,Cuz_ImBloodNight,CuzImSweaZy,D4rkPixel,Dark_Knight_TV,Darknightkill_,DatGuyMxrco,DerFelixGermany,DerGommieZug,DerMeister_HD,Destroyer1Aut,Dezay,DieGamingStubeYT,DomiNord,du_bist_dumm,EinfachNope,Expertpvp,FeelMyRodHits,FeelThePlayerTV,Fisxh,GottSevox,HaenseleGHG,Hamster32,Huogu,HvnlyFan,IchBinAmRxshen,IchmagCake,IM2000,IronSmiley,ITzHqZ,ItzRifted,ixBilly,jansengame,JasonWinner,JHammer17,jojoachimW,JuSch813,Kaiyodo,KarlPupst,KevzoxSub,KnzTi,LeLon9,LeonFNS,LeqitLukes,leSemybond,LetsPlayArea,lexerxes,Loudjunkie,Lukas_ZTX,luke19681968,LuuuuSub,MassaBassa,maxdas,Maxi_Mitte,Meinzz,MF15,Miking2004,Minecrafter_1a,Mosquitoooooo,NotWithRxffy_y,OfficialNickYT,PacmannHD,PalutenTQ,PascalMaxi,Phutur_Clown,pierre_fsx,Rechtshreibfeler,Red_Twins,SaintNate22,SaiyajinGodGHG,Sariux,SchachExpertAT,Seemy,ShadowPlaysHDYT,ShvtTheFxckUp,SILBERBERETTA,SkywarsGott,SllimeCrafterTv,Stinker178,Stumpfi_,TheBotKiller,ThePic_,TheSwift_,Thor05,TimeForCNFSD,Tonkey06,UFOKUMO,VeNeMxDERxTL,xImStrange,XITZZH4H4PVP,xKazyyy,xKrieger_,XPhoenixOfFireX,xXLukaPvPxX,xXMaityXx,xXpvp_Tim3105_Xx,xXtoiliaPVPXx,Yitre,YoungHustenjuice,ZelotesGHG,Zendus69,ZMCMarco,ZuLegit,zwirbli";
			}
			String[] names = NameList.split(",");
			Random rnd = new Random();
			return names[rnd.nextInt(names.length)];
		} else {
			Random rnd = new Random();
			String name = nicknames.get(rnd.nextInt(nicknames.size()));
			return name;
		}
	}
	
	
}
