package com.example.data.model

import java.util.UUID

data class PerformanceMetrics(
    val breathabilityPercent: Int, // 0 - 100
    val weightGrams: Int,
    val energyReturnPercent: Int, // 0 - 100
    val stabilityPercent: Int, // 0 - 100
    val weatherResistancePercent: Int, // 0 - 100
    val durabilityPercent: Int // 0 - 100
)

data class ProductSystem(
    val id: String,
    val name: String,
    val category: String, // "Footwear", "Apparel", "Hardware"
    val price: Double,
    val subName: String, // German industrial concept
    val collection: String, // "Berlin Performance", "Alpine Engineering", "Football Heritage", "Future Motion"
    val techPrimaryName: String, // e.g., "Futurecraft 4D Carbon Grid"
    val techSecondaryName: String, // e.g., "Primeknit Adaptive Weave"
    val techDescription: String,
    val engineeringStory: String,
    val researchProcess: String,
    val testingInsight: String,
    val sustainabilityCarbonKg: Double, // e.g., 2.8 kg CO2e
    val recycledPercent: Int, // e.g., 74%
    val metrics: PerformanceMetrics,
    val isLimitedRelease: Boolean = false,
    val launchTimestamp: Long = 0, // 0 if already released, or future millis
    val isMemberOnly: Boolean = false,
    val vectorSilhouetteId: String // "runner", "cleat", "jacket", "hoodie"
)

object SampleData {
    val COLLECTIONS_LIST = listOf(
        "Berlin Performance",
        "Alpine Engineering",
        "Football Heritage",
        "Future Motion"
    )

    val PRODUCTS = listOf(
        ProductSystem(
            id = "product_eqt_carbon_4d",
            name = "EQT CARBON 4D",
            category = "Footwear",
            price = 450.00,
            subName = "Berlin Lab System - Ref. 09-4D",
            collection = "Berlin Performance",
            techPrimaryName = "Futurecraft 4D Carbon Mesh",
            techSecondaryName = "Hydro-Shield Protective Cage",
            techDescription = "Engineered digital light synthesis creates an ultra-durable carbon-lattice midsole tuned specifically for urban concrete deceleration.",
            engineeringStory = "Inspired by the architectural geometry of Berlin's Bauhaus museum, the EQT Carbon 4D is formed of single-piece technical meshes paired with liquid-formed lattices.",
            researchProcess = "3,400 hours of computational testing at the Herzogenaurach sports science pavilion with high-impact urban sprinters.",
            testingInsight = "88% increase in heel-strike stability compared to linear foam structures in technical wet-weather testing.",
            sustainabilityCarbonKg = 3.6,
            recycledPercent = 82,
            metrics = PerformanceMetrics(
                breathabilityPercent = 95,
                weightGrams = 224,
                energyReturnPercent = 88,
                stabilityPercent = 90,
                weatherResistancePercent = 70,
                durabilityPercent = 94
            ),
            isLimitedRelease = true,
            launchTimestamp = System.currentTimeMillis() + 86400000 * 2, // 2 days in future
            isMemberOnly = true,
            vectorSilhouetteId = "runner"
        ),
        ProductSystem(
            id = "product_monolith_shell",
            name = "MONOLITH ALPINE SHELL",
            category = "Apparel",
            price = 650.00,
            subName = "Alpine Grid System - Ref. AP-02",
            collection = "Alpine Engineering",
            techPrimaryName = "Gore-Tex Pro 3-Layer Membrane",
            techSecondaryName = "Heated Aerogel Thermo-Panels",
            techDescription = "A lightweight technical shell designed for extreme mountaineering. Includes breathable smart-pores that close under high cold index.",
            engineeringStory = "Engineered in partnership with elite German alpine rescue organizations. Form follows absolute technical survival requirements, utilizing minimalist seam construction.",
            researchProcess = "Developed via physical thermographic mapping in cold chamber wind tunnels on Zugspitze glacier.",
            testingInsight = "Impermeable to 20,000mm water columns while retaining maximum ventilation inside high-exertion zones.",
            sustainabilityCarbonKg = 5.2,
            recycledPercent = 95,
            metrics = PerformanceMetrics(
                breathabilityPercent = 80,
                weightGrams = 380,
                energyReturnPercent = 40,
                stabilityPercent = 75,
                weatherResistancePercent = 100,
                durabilityPercent = 98
            ),
            isLimitedRelease = false,
            isMemberOnly = false,
            vectorSilhouetteId = "jacket"
        ),
        ProductSystem(
            id = "product_kaiser_elite_fg",
            name = "KAISER ELITE FG",
            category = "Footwear",
            price = 320.00,
            subName = "Stadium Precision System - Ref. KM-10",
            collection = "Football Heritage",
            techPrimaryName = "Primeknit-Skin Compression Upper",
            techSecondaryName = "SpeedFrame Carbon Traction Chassis",
            techDescription = "Ultra-premium football chassis constructed from aerospace-grade carbon fiber and adaptive compression k-knit for surgical ball manipulation.",
            engineeringStory = "Honoring forty years of German tournament excellence. Crafted to visual minimalist perfection, inspired by the grand Munich stadium structures.",
            researchProcess = "Direct engineering feedback from top-flight Champions League midfield players during simulated high-cadence matches.",
            testingInsight = "99.4% power transfer retention throughout rapid directional pivots on standard natural grass pitches.",
            sustainabilityCarbonKg = 2.4,
            recycledPercent = 60,
            metrics = PerformanceMetrics(
                breathabilityPercent = 85,
                weightGrams = 178,
                energyReturnPercent = 92,
                stabilityPercent = 95,
                weatherResistancePercent = 65,
                durabilityPercent = 89
            ),
            isLimitedRelease = false,
            isMemberOnly = true,
            vectorSilhouetteId = "cleat"
        ),
        ProductSystem(
            id = "product_future_mesh_vest",
            name = "AEROLITE LAB SWEATSHIRT",
            category = "Apparel",
            price = 220.00,
            subName = "Dynamic Fit System - Ref. FM-77",
            collection = "Future Motion",
            techPrimaryName = "DryHeat Reactive Kinetic Grid",
            techSecondaryName = "Antibacterial Silver-Weave Yarn",
            techDescription = "Adaptive sportswear featuring micro-ribbed channels that physically widen as body temperature increases to allow targeted thermal release.",
            engineeringStory = "Synthesizing Bauhaus principles with future digital sports science, this layer uses physical geometric kinetics to control comfort, omitting bulk.",
            researchProcess = "Sweat-mapping and metabolic testing with elite decathletes inside closed-cycle hypoxic athletic chambers.",
            testingInsight = "Maintains optimal 36.5°C body core temperature during high-intensity metabolic training sessions.",
            sustainabilityCarbonKg = 1.9,
            recycledPercent = 100,
            metrics = PerformanceMetrics(
                breathabilityPercent = 98,
                weightGrams = 145,
                energyReturnPercent = 50,
                stabilityPercent = 60,
                weatherResistancePercent = 30,
                durabilityPercent = 85
            ),
            isLimitedRelease = false,
            isMemberOnly = false,
            vectorSilhouetteId = "hoodie"
        ),
        ProductSystem(
            id = "product_prime_r1_hybrid",
            name = "PRIME R1 INTEGRATED CHASSIS",
            category = "Footwear",
            price = 380.00,
            subName = "Stadt-Sport Performance - Ref. R1-X",
            collection = "Berlin Performance",
            techPrimaryName = "Dual-Density BOOST Energy Core",
            techSecondaryName = "Continental Premium Rubber Frame",
            techDescription = "Integrating high-bounce energy return cells with automotive grade traction structures for supreme response across varying metropolitan surfaces.",
            engineeringStory = "Designed to transition seamlessly from high-performance sprint cycles directly into structural architecture appreciation.",
            researchProcess = "Acoustic and vibrational impact analysis over cobblestone, paving tiles, and glass corridors.",
            testingInsight = "Reduces heel deceleration load by 26%, dramatically easing lower back muscular fatigue.",
            sustainabilityCarbonKg = 2.9,
            recycledPercent = 75,
            metrics = PerformanceMetrics(
                breathabilityPercent = 92,
                weightGrams = 240,
                energyReturnPercent = 95,
                stabilityPercent = 85,
                weatherResistancePercent = 80,
                durabilityPercent = 92
            ),
            isLimitedRelease = true,
            launchTimestamp = System.currentTimeMillis() + 640000000,
            isMemberOnly = false,
            vectorSilhouetteId = "runner"
        )
    )
}
