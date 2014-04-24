var DB = {
	listes : [
		{
            dateRedaction : "10/12/2013",
            dateCourse : "13/12/2013",
            courses : [
                {designation : "lait", qte : "1l"},
                {designation : "beurre", qte : "250g"}
            ]
        },
        {
            dateRedaction : "10/12/2013",
            dateCourse : "13/12/2013",
            courses : [
                {designation : "lait", qte : "1l"},
                {designation : "beurre", qte : "250g"}
            ]
        }
    ]
	,
	parametres : {
		"produits": [ 
		{ "designation": "activia" , "categorie": "Desserts" }, 
		{ "designation": "beurre", "categorie": "Crèmerie" }, 
		{ "designation": "biscuits", "categorie": "Apéritifs" }, 
		{ "designation": "bière" , "categorie": "Boisson" },
		{ "designation": "brioche", "categorie": "Petit déjeuner" }, 
		{ "designation": "cacahuètes", "categorie": "Apéritifs" }, 
		{ "designation": "café", "categorie": "Petit déjeuner" }, 
		{ "designation": "chips", "categorie": "Apéritifs"  }, 
		{ "designation": "coca" , "categorie": "Boisson" }, 
		{ "designation": "compote", "categorie": "Desserts"  },
		{ "designation": "confiture", "categorie": "Petit déjeuner" }, 
		{ "designation": "cornichons", "categorie": "Condiments" }, 
		{ "designation": "craquant chocolat" , "categorie": "Desserts" }, 
		{ "designation": "crème aux oeufs" , "categorie": "Desserts" }, 
		{ "designation": "crème fraîche", "categorie": "Crèmerie" }, 
		{ "designation": "Fanta"  , "categorie": "Boisson"}, 
		{ "designation": "farine", "categorie": "Préparation culinaire" }, 
		{ "designation": "fromage", "categorie": "Fromage" }, 
		{ "designation": "gruyère rapé", "categorie": "Fromage"},
		{ "designation": "huile", "categorie": "Condiments" }, 
		{ "designation": "jambon", "categorie": "Charcuterie" }, 
		{ "designation": "ketchup", "categorie": "Condiments" }, 
		{ "designation": "kinder pingui" , "categorie": "Desserts" }, 
		{ "designation": "lait demi-écrémé", "categorie": "Crèmerie"}, 
		{ "designation": "lait entier", "categorie": "Crèmerie"}, 
		{ "designation": "lait écrémé", "categorie": "Crèmerie"}, 
		{ "designation": "lardons", "categorie": "Charcuterie" }, 
		{ "designation": "mayonnaise", "categorie": "Condiments" }, 
		{ "designation": "miel", "categorie": "Petit déjeuner" }, 
		{ "designation": "moutarde", "categorie": "Condiments" }, 
		{ "designation": "nutella", "categorie": "Petit déjeuner" }, 
		{ "designation": "oeufs", "categorie": "Préparation culinaire" }, 
		{ "designation": "olives", "categorie": "Apéritifs"  }, 
		{ "designation": "pain complet", "categorie": "Boulangerie-Pâtisserie"}, 
		{ "designation": "pain de mie", "categorie": "Boulangerie-Pâtisserie" }, 
		{ "designation": "plat cusiné", "categorie": "Conserves" }, 
		{ "designation": "poivre", "categorie": "Condiments" }, 
		{ "designation": "pommes de terre" , "categorie": "Légumes"  },
		{ "designation": "purée" , "categorie": "Pâtes-Riz"  }, 
		{ "designation": "pâte à tarte", "categorie": "Frais"},
		{ "designation": "pâtes cappellini" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes coquilletes" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes fraîches", "categorie": "Frais" }, 
		{ "designation": "pâtes macaroni" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes papillon" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes pennes" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes spaghetti" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes tagliatelles" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâtes torsettes" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "pâté", "categorie": "Charcuterie" }, 
		{ "designation": "ri risotto" , "categorie": "Pâtes-Riz"  }, 
		{ "designation": "riz basmati" , "categorie": "Pâtes-Riz"  }, 
		{ "designation": "riz riz au lait" , "categorie": "Pâtes-Riz"  }, 
		{ "designation": "riz" , "categorie": "Pâtes-Riz"  }, 
		{ "designation": "rosé"  , "categorie": "Boisson"}, 
		{ "designation": "saucisses", "categorie": "Charcuterie" }, 
		{ "designation": "saucisson", "categorie": "Charcuterie" }, 
		{ "designation": "sel", "categorie": "Condiments" }, 
		{ "designation": "semoule" , "categorie": "Pâtes-Riz" }, 
		{ "designation": "soupe", "categorie": "Soupes" }, 
		{ "designation": "sucre", "categorie": "Préparation culinaire" }, 
		{ "designation": "thé", "categorie": "Petit déjeuner" }, 
		{ "designation": "viennois" , "categorie": "Desserts" }, 
		{ "designation": "vinaigre", "categorie": "Condiments" }, 
		{ "designation": "whisky" , "categorie": "Boisson"}, 
		{ "designation": "yaourt fruits", "categorie": "Desserts"  },
		{ "designation": "yaourt nature" , "categorie": "Desserts" },  
		{ "designation": "yaourt vanille" , "categorie": "Desserts" }
		]
		,
		"unites": [
		{ "designation": "bocal" }, 
		{ "designation": "bouteille" }, 
		{ "designation": "boîte" }, 
		{ "designation": "brique" }, 
		{ "designation": "flacon" }, 
		{ "designation": "g" }, 
		{ "designation": "kg" }, 
		{ "designation": "l" }, 
		{ "designation": "pack" },
		{ "designation": "paquet" }, 
		{ "designation": "pot" }, 
		{ "designation": "sachet" }, 
		{ "designation": "tranche" },
		{ "designation": "tube" }
		]	
	}
	,
	menu : {
	  libelle : "Soufflet au fromage",
	  ingredients : [
			{designation : "gruyère"},
			{designation : "yaourt nature"},
			{designation : "lardons"}
		]
	}
}

