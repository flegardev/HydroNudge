# Room and AndroidX supply their own consumer rules.
# Do not broadly keep app classes; exercise the optimized release before shipping.

# Glance instantiates this action by class name and a no-argument constructor.
# Confirmed in the optimized launcher-widget smoke test: R8 removed <init>().
-keep class app.hydronudge.widgets.WidgetDrinkAction {
    public <init>();
}
