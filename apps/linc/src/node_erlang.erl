-module(node_erlang).
-export([get_mailbox/0,
         configures_linc/0
]).

-export([configures_enetconf/0]).

-export([configures_lager/0]).

-export([configures_sasl/0]).

-export([read_excluded_modules/0]).

-export([transmit_configures_linc/0,
         transmit_configures_enetconf/0,
         transmit_configures_lager/0,
         transmit_configures_sasl/0
]).

%% ---------------------------configure reading module ------------------------------
%% Parameters read from linc in sys.config. 
read_of_config() ->
    {ok, IsOfConfigEnable} = application:get_env(linc, of_config),
    {of_config, IsOfConfigEnable}.

read_sync_routing() ->
    {ok, IsSynRoutingTrue} = application:get_env(linc, sync_routing),
    {sync_routing,IsSynRoutingTrue}.

get_mailbox() ->
    {ok,Mailbox} = application:get_env(linc, mailbox),
    Mailbox.

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

%%----------------------------configuresread----------------------------------------
configures_linc() ->
    Tuple_Linc_Configures = {linc,
                            [read_of_config(),
                             read_sync_routing(),
                             read_capable_switch_ports(),
                             read_capable_switch_queues(),
                             read_logical_switches()
                            ]}.

configures_enetconf() ->
    Tuple_Enetconf_Configures = {enetconf,
                                [read_capabilities(),
                                 read_callback_module(),
                                 read_sshd_ip(),
                                 read_sshd_port(),
                                 read_sshd_user_passwords()
                                ]}.

configures_lager() ->
    Tuple_Lager_Configures = {lager,
                             [read_colored(),
                              read_handlers()
                            ]}.
    
configures_sasl() ->
    Tuple_Sasl_Configures = {sasl,
                            [read_sasl_error_logger(),
                             read_errlog_type(),
                             read_error_logger_mf_dir(),
                             read_error_logger_mf_maxbytes(),
                             read_error_logger_mf_maxfiles()
                            ]}.




%%-----------------------------------------------------------------------------------




%%----------------------------transmitting module------------------------------------
%Host_Name = get_host_name().





transmit_configures_linc() ->
    get_mailbox() ! {self(), configures_linc()},
    receive {Mbox, Msg} -> Msg end.

transmit_configures_enetconf() ->
    get_mailbox() ! {self(), configures_enetconf()},
    receive {Mbox, Msg} -> Msg end.

transmit_configures_lager() ->
    get_mailbox() ! {self(), configures_lager()},
    receive {Mbox, Msg} -> Msg end.

transmit_configures_sasl() ->
    get_mailbox() ! {self(), configures_sasl()},
    receive {Mbox, Msg} -> Msg end.







%%-----------------------------------------------------------------------------------


