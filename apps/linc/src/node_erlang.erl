-module(node_erlang).
-export([read_of_config/0,
         read_sync_routing/0,
         read_capable_switch_ports/0,
         read_capable_switch_queues/0,
         read_logical_switches/0

]).

-export([read_capabilities/0,
         read_callback_module/0,
         read_sshd_ip/0,
         read_sshd_port/0,
         read_sshd_user_passwords/0
]).

-export([read_colored/0,
         read_handlers/0
]).

-export([read_sasl_error_logger/0,
         read_errlog_type/0,
         read_error_logger_mf_dir/0,
         read_error_logger_mf_maxbytes/0,
         read_error_logger_mf_maxfiles/0
]).

-export([read_excluded_modules/0]).

%% ---------------------------configure reading module ------------------------------
%% Parameters read from linc in sys.config. 
read_of_config() ->
    {ok, IsOfConfigEnable} = application:get_env(linc, of_config),
    {of_config, IsOfConfigEnable}.

read_sync_routing() ->
    {ok, IsSynRoutingTrue} = application:get_env(linc, sync_routing),
    {sync_routing,IsSynRoutingTrue}.

read_capable_switch_ports() ->
    {ok, CapableSwitchPorts} = application:get_env(linc, capable_switch_ports),
    {capable_switch_ports, CapableSwitchPorts}.

read_capable_switch_queues() ->
    {ok, CapableSwitchQueues} = application:get_env(linc, capable_switch_queues),
    {capable_switch_queues, CapableSwitchQueues}.

read_logical_switches() ->
    {ok, LogicalSwitches} = application:get_env(linc, logical_switches),
    {logical_switches, LogicalSwitches}.

%% Parameters read from epcap in sys.config. 


%% Parameters read from of_protocal in sys.config. 


%% Parameters read from enetconf in sys.config. 
read_capabilities() ->
    {ok, Capabilities} = application:get_env(enetconf, capabilities),
    {capabilities, Capabilities}.

read_callback_module() ->
    {ok,CallbackModule} = application:get_env(enetconf, callback_module),
    {callback_module, CallbackModule}.

read_sshd_ip() ->
    {ok,SshdIp} = application:get_env(enetconf, sshd_ip),
    {sshd_ip, SshdIp}.

read_sshd_port() ->
    {ok, SshdPort} = application:get_env(enetconf, sshd_port),
    {sshd_port, SshdPort}.

read_sshd_user_passwords() ->
    {ok, SshdUserPasswords} = application:get_env(enetconf, sshd_user_passwords),
    {sshd_user_passwords, SshdUserPasswords}.

%% Parameters read from lager in sys.config.
read_colored() ->
    {ok, Colored} = application:get_env(lager, colored),
    {colored, Colored}.

read_handlers() ->
    {ok, Handlers} = application:get_env(lager, handlers),
    {handlers, Handlers}.

%% Parameters read from sasl in sys.config.
read_sasl_error_logger() ->
    {ok, SaslErrorLogger} = application:get_env(sasl, sasl_error_logger),
    {sasl_error_logger, SaslErrorLogger}.

read_errlog_type() ->
    {ok, ErrlogType} = application:get_env(sasl, errlog_type),
    {errlog_type, ErrlogType}.

read_error_logger_mf_dir() ->
    {ok, ErrorLoggerMfDir} = application:get_env(sasl, error_logger_mf_dir),
    {error_logger_mf_dir,ErrorLoggerMfDir}.

read_error_logger_mf_maxbytes() ->
    {ok, ErrorLoggerMfMaxbytes} = application:get_env(sasl, error_logger_mf_maxbytes),
    {error_logger_mf_maxbytes, ErrorLoggerMfMaxbytes}.

read_error_logger_mf_maxfiles() ->
    {ok, ErrorLoggerMfMaxfiles} = application:get_env(sasl, error_logger_mf_maxfiles),
    {error_logger_mf_maxfiles, ErrorLoggerMfMaxfiles}.

%% Parameters read from sync in sys.config.
read_excluded_modules() ->
    {ok, ExcludedModules} = application:get_env(sync, excluded_modules),
    {excluded_modules, ExcludedModules}.
%%-----------------------------------------------------------------------------------

%%----------------------------transmitting module------------------------------------






%%-----------------------------------------------------------------------------------


