package com.rei.searchengine.cli;

import com.rei.searchengine.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ResolverTest {

    @Mock
    SearchService searchService;
    Resolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resolver = new Resolver(searchService);
    }

    @Test
    void testForUnknownCommand() {
        MockedStatic<Printer> mockedPrinter = mockStatic(Printer.class);
        resolver.resolve("unknown");
        mockedPrinter.verify(() -> Printer.commandUnknown("unknown"), times(1));
        mockedPrinter.closeOnDemand();
    }

    @Test
    void testHelpCommand() {
        MockedStatic<Printer> mockedPrinter = mockStatic(Printer.class);

        //Command is missing
        resolver.resolve("help");
        mockedPrinter.verify(Printer::printCommands, times(1));

        //Command is INDEX
        resolver.resolve("help index");
        mockedPrinter.verify(Printer::indexHelp, times(1));

        //CommandType is QUERY
        resolver.resolve("help query");
        mockedPrinter.verify(Printer::queryHelp, times(1));

        //CommandType is QUIT
        resolver.resolve("help quit");
        mockedPrinter.verify(Printer::quitHelp, times(1));

        //CommandType is HELP
        resolver.resolve("help help");
        mockedPrinter.verify(Printer::help, times(1));

        mockedPrinter.closeOnDemand();
    }

    @Test
    public void resolveIndexCommand() {
        doNothing().when(searchService).index(anyLong(), anyList());

        //no arguments
        resolver.resolve("index");
        verify(searchService, times(0)).index(anyLong(), any());

        //doc_id not number
        resolver.resolve("index invalid test");
        verify(searchService, times(0)).index(any(), any());

        //token contains unsupported characters
        resolver.resolve("index 1 test$");
        verify(searchService, times(0)).index(any(), any());

        //correct values
        resolver.resolve("index 1 test");
        verify(searchService, times(1)).index(any(), anyList());
    }

    @Test
    void resolveQueryCommand() {
        //without arguments
        resolver.resolve("query");
        verify(searchService, times(0)).query(any());

        //unsupported characters
        resolver.resolve("query milk#");
        verify(searchService, times(0)).query(any());

        //correct values
        resolver.resolve("query milk&honey");
        verify(searchService, times(1)).query(any());
    }

}
