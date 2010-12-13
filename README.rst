=========
AdminTool
=========
A tool for administration of an hMod-enabled Minecraft server
#############################################################

----------
Installing
----------
To install AdminTool, just put AdminTool.jar in the ``plugins`` folder in your
hMod installation. If you want it to load by default, add ``AdminTool`` to
``plugins`` in the file ``server.properties``. If there is already a value set,
add ``,AdminTool`` to the end.

-----------
Configuring
-----------
AdminTool currently has no configuration. You can, however, restrict access to
individual tools with hMod's permission system. For any tool to be used, the
user/group must have permission to use ``/admintool``. In addition, the
permission to use ``/admintool_TOOLNAME`` (i.e. ``/admintool_destroyblock``
for the ``destroyblock`` tool) must be present.

-----
Using
-----
Saying ``/admintool`` or ``/adm`` should present a list of tools that you are
permitted to use with your current permissions. ``/admintool TOOLNAME`` will
show help for that individual tool.

