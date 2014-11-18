package com.expedia.seiso.domain.repo.search.query.token;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class SpaceDelimitedDatabaseWildCardTokenizerUnitTest
{

    @Test
    public void testTokenize()
    {
        String termString = "this is a test";
        Set<String> expected = new LinkedHashSet<String>( Arrays.asList( new String[]{ "%this%", "%is%", "%a%", "%test%" } ) );
        SpaceDelimitedDatabaseWildCardTokenizer spaceDelimitedDatabaseWildCardTokenizer = new SpaceDelimitedDatabaseWildCardTokenizer();
        Set<String> actual = spaceDelimitedDatabaseWildCardTokenizer.tokenize( termString );
        
        Assert.assertEquals( expected, actual );
        
    }
    
    @Test
    public void testEmptyTermString()
    {
        String termString = "";
        Set<String> expected = new LinkedHashSet<String>( Arrays.asList( new String[]{} ) );
        SpaceDelimitedDatabaseWildCardTokenizer spaceDelimitedDatabaseWildCardTokenizer = new SpaceDelimitedDatabaseWildCardTokenizer();
        Set<String> actual = spaceDelimitedDatabaseWildCardTokenizer.tokenize( termString );
        
        Assert.assertEquals( expected, actual );
    }
    
    @Test
    public void testNullTermString()
    {
        String termString = null;
        Set<String> expected = new LinkedHashSet<String>( Arrays.asList( new String[]{} ) );
        SpaceDelimitedDatabaseWildCardTokenizer spaceDelimitedDatabaseWildCardTokenizer = new SpaceDelimitedDatabaseWildCardTokenizer();
        Set<String> actual = spaceDelimitedDatabaseWildCardTokenizer.tokenize( termString );
        
        Assert.assertEquals( expected, actual );
    }
    
    @Test
    public void testBlankString()
    {
        String termString = "   ";
        Set<String> expected = new LinkedHashSet<String>( Arrays.asList( new String[]{} ) );
        SpaceDelimitedDatabaseWildCardTokenizer spaceDelimitedDatabaseWildCardTokenizer = new SpaceDelimitedDatabaseWildCardTokenizer();
        Set<String> actual = spaceDelimitedDatabaseWildCardTokenizer.tokenize( termString );
        
        Assert.assertEquals( expected, actual );
    }

}
