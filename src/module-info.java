module pgfCore {
	exports net.pgfmc.core.requestAPI;
	exports net.pgfmc.core.configify;
	exports net.pgfmc.core.cmd.admin;
	exports net.pgfmc.core.playerdataAPI;
	exports net.pgfmc.core.cmd.donator;
	exports net.pgfmc.core;
	exports net.pgfmc.core.permissions;
	exports net.pgfmc.core.cmd;
	exports net.pgfmc.core.inventoryAPI;
	exports net.pgfmc.core.report;

	requires java.logging;
	requires transitive org.bukkit;
}