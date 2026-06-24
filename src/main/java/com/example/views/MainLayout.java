// src/main/java/com/contaminacion/views/MainLayout.java
package com.example.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().set("margin-right", "1rem");

        H1 title = new H1("🌍 Sistema de Gestión de Contaminación");
        title.getStyle().set("font-size", "1.5rem");
        title.getStyle().set("margin", "0");

        header.add(drawerToggle, title);
        addToNavbar(header);

        // Drawer (menú lateral)
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        tabs.add(createTab("📊 Dashboard", DashboardView.class));
        tabs.add(createTab("🔮 Predicción 24h", PrediccionView.class));
        tabs.add(createTab("🚨 Alertas", AlertasView.class));
        tabs.add(createTab("📈 Histórico vs OMS", HistoricoView.class));
        tabs.add(createTab("📄 Reportes", ReporteView.class));

        addToDrawer(tabs);
    }

    private Tab createTab(String label, Class<?> viewClass) {
        RouterLink link = new RouterLink(label, viewClass);
        link.setTabIndex(-1);
        return new Tab(link);
    }
}