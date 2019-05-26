package base.cor.handler;

public class PriceHandlerFactory {

    /**
     * single duty: factory method
     *
     * @return
     */
    public static PriceHandler createPriceHandler() {
        PriceHandler saleMan = new SaleManHandler();
        PriceHandler leader = new LeaderHandler();
        PriceHandler manager = new ManagerHandler();
        PriceHandler director = new DirectorHandler();
        PriceHandler vicePresident = new VicePresidentHandler();
        PriceHandler ceo = new CEOHandler();

        //set the successor
        saleMan.setSuccessor(leader);
        leader.setSuccessor(manager);
        manager.setSuccessor(director);
        director.setSuccessor(vicePresident);
        vicePresident.setSuccessor(ceo);

        return saleMan;
    }
}
