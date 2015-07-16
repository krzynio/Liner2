package g419.spatial.filter;

import g419.spatial.io.SpatialPatternParser;
import g419.spatial.structure.SpatialRelation;
import g419.spatial.structure.SpatialRelationPattern;
import g419.spatial.structure.SpatialRelationPatternMatcher;
import g419.toolbox.sumo.Sumo;
import g419.toolbox.sumo.WordnetToSumo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.zip.DataFormatException;

public class RelationFilterSemanticPattern implements IRelationFilter {

	WordnetToSumo wts = null;
	Sumo sumo = null;
	SpatialRelationPatternMatcher patternMatcher = null;
		
	public RelationFilterSemanticPattern() throws IOException{

        try {
			this.wts = this.getWordnetToSumo();
	        this.patternMatcher = this.getPatternMatcher();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DataFormatException
	 */
	private WordnetToSumo getWordnetToSumo() throws IOException, DataFormatException{
		String location = "/g419/spatial/resources/mapping-26.05.2015-Serdel.csv";
		InputStream resource = this.getClass().getResourceAsStream(location);

        if (resource == null) {
            throw new MissingResourceException("Resource not found: " + location,
                    this.getClass().getName(), location);
        }
        Reader serdelReader = new InputStreamReader( resource );
        return new WordnetToSumo(serdelReader);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private SpatialRelationPatternMatcher getPatternMatcher() throws IOException{
		String location = "/g419/spatial/resources/spatial_relation_patterns.txt";
		InputStream resource = this.getClass().getResourceAsStream(location);

        if (resource == null) {
            throw new MissingResourceException("Resource not found: " + location,
                    this.getClass().getName(), location);
        }
        
        SpatialPatternParser parser = new SpatialPatternParser(new InputStreamReader( resource ), new Sumo(false));
        return parser.parse();        
	}
		
	@Override
	public boolean pass(SpatialRelation relation) {		
		String landmark = relation.getLandmark().getSentence().getTokens().get(relation.getLandmark().getHead()).getDisambTag().getBase();
		String trajector = relation.getTrajector().getSentence().getTokens().get(relation.getTrajector().getHead()).getDisambTag().getBase();
		Set<String> landmarkConcepts = this.wts.getConcept(landmark);
		Set<String> trajetorConcepts = this.wts.getConcept(trajector);
		
		if ( landmarkConcepts != null ){ 
			relation.getLandmarkConcepts().addAll(landmarkConcepts);
		}
		
		if ( trajetorConcepts != null ){
			relation.getTrajectorConcepts().addAll(trajetorConcepts);
		}
		
		List<SpatialRelationPattern> matching = this.patternMatcher.matchAll(relation);
				
		if ( matching.size() == 0){
//			System.out.println("\t\t\tTrajector = " + trajector + " => " + String.join(", ", relation.getTrajectorConcepts()));
//			System.out.println("\t\t\tLandmark  = " + landmark + " => " + String.join(", ",relation.getLandmarkConcepts()));
		}
		return matching.size() > 0;
	}
	
}